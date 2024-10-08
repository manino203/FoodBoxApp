package com.example.foodboxapp.backend.network

import com.example.foodboxapp.R
import com.example.foodboxapp.backend.data_holders.Account
import com.example.foodboxapp.backend.data_holders.Address
import com.example.foodboxapp.backend.data_holders.CartItem
import com.example.foodboxapp.backend.data_holders.DataHolderSerializer
import com.example.foodboxapp.backend.data_holders.Order
import com.example.foodboxapp.backend.data_holders.Product
import com.example.foodboxapp.backend.data_holders.Store
import com.example.foodboxapp.util.Logger
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthEmailException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthRecentLoginRequiredException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.FirebaseFirestoreSettings
import com.google.firebase.firestore.MemoryCacheSettings
import com.google.firebase.firestore.Query
import kotlinx.coroutines.tasks.await

interface FoodBoxService {
    suspend fun fetchAccount(uid: String): Account
    suspend fun updateAccount(acc: Account): Result<Account>
    suspend fun login(email: String, password: String): Result<Account>
    suspend fun resumeSession(): Result<Account>
    suspend fun register(email: String, password: String): Result<Account>
    suspend fun logout(): Result<Unit>

    suspend fun fetchStores(): Result<List<Store>>

    suspend fun fetchProducts(storeId: String): Result<List<Product>>

    suspend fun sendOrder(order: Order): Result<Unit>
    suspend fun fetchAvailableOrders(): Result<List<Order>>
    suspend fun fetchAcceptedOrders(uid: String): Result<List<Order>>
    suspend fun acceptOrder(orderId: String, uid: String): Result<Unit>
    suspend fun completeOrder(orderId: String): Result<Unit>
}

class FoodBoxServiceImpl(
    private val networkChecker: NetworkAvailabilityChecker
) : FoodBoxService{

    private val db = FirebaseFirestore.getInstance().apply {
        FirebaseFirestoreSettings.Builder().setLocalCacheSettings(
            MemoryCacheSettings.newBuilder()
                .build()
        ).build()
    }
    private val auth = FirebaseAuth.getInstance()


    override suspend fun fetchAccount(uid: String): Account {
        return db.collection("accounts").document(uid).get().await().let {
            DataHolderSerializer.deserializeAccount(uid, it)
        }
    }

    @Suppress("unused")
    override suspend fun updateAccount(acc: Account): Result<Account> {
        return runWithExceptionHandling(true){
            acc.also { updateAccountInDb(it) }
        }
    }

    private suspend fun updateAccountInDb(acc: Account){
        db.collection("accounts").document(acc.id).set(
            DataHolderSerializer.serializeAccount(acc)
        ).await()
    }

    override suspend fun login(email: String, password: String): Result<Account> {
        return runWithExceptionHandling(false){
            fetchAccount(
                signIn(
                    auth.signInWithEmailAndPassword(email, password).await().user
                )
            )
        }
    }

    override suspend fun resumeSession(): Result<Account> {
        return runWithExceptionHandling(false){ fetchAccount(signIn(auth.currentUser)) }

    }

    override suspend fun register(email: String, password: String): Result<Account> {
        return runWithExceptionHandling(false){
            auth.createUserWithEmailAndPassword(email, password).await().user?.uid?.let { uid ->
                Account(
                    id = uid,
                    email = email
                ).also{
                    updateAccountInDb(
                        it
                    )
                }
            } ?: throw LocalizedException(R.string.failed_to_create_account)
        }
    }

    override suspend fun logout(): Result<Unit> {
        return runWithExceptionHandling(false){
            auth.signOut()
        }
    }

    override suspend fun fetchStores(): Result<List<Store>> {
        return runWithExceptionHandling {
            db.collection("stores").get().await().documents.map {
                DataHolderSerializer.deserializeStore(it)
            }
        }
    }

    override suspend fun fetchProducts(storeId: String): Result<List<Product>> {
        return runWithExceptionHandling{
            db.collection("products").whereEqualTo("storeId", storeId).get().await().documents.mapNotNull {
                DataHolderSerializer.deserializeProduct(it)
            }
        }
    }

    override suspend fun sendOrder(order: Order): Result<Unit> {
        return runWithExceptionHandling(true){
            db.collection("orders").document().set(
                DataHolderSerializer.serializeOrder(order)
            )
        }
    }

    override suspend fun fetchAvailableOrders(): Result<List<Order>> {
        return runWithExceptionHandling{
            fetchOrders(null)
        }
    }

    override suspend fun fetchAcceptedOrders(uid: String): Result<List<Order>> {
        return runWithExceptionHandling{
            fetchOrders(uid)
        }
    }

    override suspend fun acceptOrder(orderId: String, uid: String): Result<Unit> {
        return runWithExceptionHandling(true){
            db.collection("orders").document(orderId).update("workerId", uid)
                .addOnFailureListener { Logger.logDebug("order_accept", "$it") }
                .await()
        }
    }

    override suspend fun completeOrder(orderId: String): Result<Unit> {
        return runWithExceptionHandling(){
            db.collection("orders").document(orderId).delete().await()
        }
    }

    private suspend fun fetchOrders(workerId: String?): List<Order> {
        return db.collection("orders")
            .whereEqualTo("workerId", workerId)
            .orderBy("timestamp", Query.Direction.ASCENDING)
            .get()
            .await()
                .mapNotNull { document ->
                    DataHolderSerializer.log("Order", document)
                    @Suppress("UNCHECKED_CAST") val cartItems: List<CartItem>? =
                        (document.get("items") as? List<Map<String, *>>)?.mapNotNull {
                            DataHolderSerializer.deserializeProduct(
                                db.collection("products").document(it["productId"].toString()).get()
                                    .await()
                            )
                                ?.let { product ->
                                    CartItem(
                                        product,
                                        count = it["count"].toString().toInt(),
                                        DataHolderSerializer.deserializeStore(
                                            db.collection("stores").document(product.storeId).get()
                                                .await()
                                        )
                                    )
                                }
                        }
                    cartItems?.let { items ->
                        Order(
                            items = items,
                            id = document.id,
                            address = Address.fromMap(document.get("address")),
                            stores = cartItems.map { it.store }.toSet().toList(),
                            timestamp = document.get("timestamp")?.toString()?.toLong()?.let {
                                Timestamp(
                                    it,
                                    0
                                )
                            },
                            total = cartItems.sumOf { it.totalPrice.toDouble() }.toFloat()
                        )
                    }

                }
    }

    private fun signIn(user: FirebaseUser?): String {
        return user?.uid ?: throw LocalizedException(R.string.sign_in_error)
    }

    private suspend fun <T>runWithExceptionHandling(checkNetwork: Boolean = true, block: suspend () -> T): Result<T>{
        return if (checkNetwork && !networkChecker.isAvailable()) {
            Result.failure(LocalizedException(R.string.network_error))
        } else {
            try {
                Result.success(block())
            } catch (e: Exception) {
                Result.failure(groupExceptions(e))
            }
        }
    }

    private fun groupExceptions(exception: Exception): Exception{

        return when(exception){
            is LocalizedException -> exception.messageId
            // Auth Errors
            is FirebaseAuthWeakPasswordException -> R.string.weak_password
            is FirebaseAuthEmailException -> R.string.invalid_email
            is FirebaseAuthInvalidCredentialsException -> R.string.invalid_credentials
            is FirebaseAuthRecentLoginRequiredException -> R.string.suspicious_activity
            is FirebaseAuthInvalidUserException -> R.string.invalid_email
            is FirebaseAuthUserCollisionException -> R.string.email_already_exists
            //Data Errors
            is ClassCastException -> {
                Logger.logError("service_exception", exception)
                R.string.issue_retrieving_data
            }
            is FirebaseFirestoreException -> {
                Logger.logError("service_exception", exception)
                R.string.database_error
            }
            //General Errors
            is FirebaseNetworkException -> {
                Logger.logError("service_exception", exception)
                R.string.network_error
            }
            else -> {
                Logger.logError("service_exception", exception)
                R.string.generic_error
            }
        }.let {
            LocalizedException(it, exception)
        }
    }
}

class LocalizedException(
    val messageId: Int,
    val originalException: Exception? = null
): Exception()



