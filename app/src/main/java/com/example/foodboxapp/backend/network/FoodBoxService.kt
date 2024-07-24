package com.example.foodboxapp.backend.network

import android.util.Log
import com.example.foodboxapp.backend.data_holders.Account
import com.example.foodboxapp.backend.data_holders.Address
import com.example.foodboxapp.backend.data_holders.CartItem
import com.example.foodboxapp.backend.data_holders.DataHolderSerializer
import com.example.foodboxapp.backend.data_holders.Order
import com.example.foodboxapp.backend.data_holders.Product
import com.example.foodboxapp.backend.data_holders.Store
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

interface FoodBoxService {
    suspend fun fetchAccount(uid: String): Account?
    suspend fun updateAccount(acc: Account): Account?
    suspend fun login(email: String, password: String): Account
    suspend fun resumeSession(): Account
    suspend fun register(email: String, password: String): Account?
    fun logout()

    suspend fun fetchStores(): List<Store>

    suspend fun fetchProducts(storeId: String): List<Product>

    suspend fun sendOrder(order: Order)
    suspend fun fetchAvailableOrders(): List<Order>
    suspend fun fetchAcceptedOrders(uid: String): List<Order>
    suspend fun acceptOrder(orderId: String, uid: String)
    suspend fun completeOrder(orderId: String)
}

class FoodBoxServiceImpl : FoodBoxService{

    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()


    override suspend fun fetchAccount(uid: String): Account {
        return try {
            db.collection("accounts").document(uid).get().await().let {
                DataHolderSerializer.deserializeAccount(uid, it)
            }
        }catch (e: Exception){
            throw groupFirestoreExceptions(e)
        }
    }

    @Suppress("unused")
    override suspend fun updateAccount(acc: Account): Account {
        try{
            db.collection("accounts").document(acc.id).set(
                DataHolderSerializer.serializeAccount(acc)
            ).await()
        }catch(e: Exception){
            throw groupFirestoreExceptions(e)
        }
        return fetchAccount(acc.id)
    }

    override suspend fun login(email: String, password: String): Account {
        return fetchAccount(signIn(auth.signInWithEmailAndPassword(email, password).await().user))
    }

    override suspend fun resumeSession(): Account {
        return fetchAccount(signIn(auth.currentUser))

    }

    override suspend fun register(email: String, password: String): Account {
        return auth.createUserWithEmailAndPassword(email, password).await().user?.uid?.let{
            updateAccount(
                Account(
                    id = it,
                    email = email
                )
            )
        } ?: throw groupAuthExceptions(NoUidException(""))
    }

    override fun logout() {
        auth.signOut()
    }

    override suspend fun fetchStores(): List<Store> {
        return try {
            db.collection("stores").get().await().documents.map {
                Log.d("Store", "title = ${it.get("title").toString()},\n" +
                        "address = ${it.get(" address ") as? Address},\n" +
                        "imageUrl = ${it.get(" title ").toString()},\n" +
                        "id = ${it.id}")
                DataHolderSerializer.deserializeStore(it)
            }
        }catch (e: Exception){
            throw groupFirestoreExceptions(e)
        }
    }

    override suspend fun fetchProducts(storeId: String): List<Product> {
        return try{
            db.collection("products").whereEqualTo("storeId", storeId).get().await().documents.mapNotNull {
                DataHolderSerializer.deserializeProduct(it)
            }
        }catch (e: Exception){
            throw groupFirestoreExceptions(e)
        }
    }

    override suspend fun sendOrder(order: Order) {
        db.collection("orders").document().set(
            DataHolderSerializer.serializeOrder(order)
        )
    }

    override suspend fun fetchAvailableOrders(): List<Order> {
        return fetchOrders(null)
    }

    override suspend fun fetchAcceptedOrders(uid: String): List<Order> {
        return fetchOrders(uid)
    }

    override suspend fun acceptOrder(orderId: String, uid: String) {
        db.collection("orders").document(orderId).update("workerId", uid).await()
    }

    override suspend fun completeOrder(orderId: String) {
        db.collection("orders").document().delete().await()
    }

    private suspend fun fetchOrders(workerId: String?): List<Order> {
        return db.collection("orders").whereEqualTo("workerId", workerId).get().await().mapNotNull { document ->
            val cartItems: List<CartItem>? = (document.get("items") as? List<Map<String, *>>)?.mapNotNull{
                DataHolderSerializer.deserializeProduct(db.collection("products").document(it["productId"].toString()).get().await())
                    ?.let { product ->
                        CartItem(
                            product,
                            count = it["count"].toString().toInt(),
                            DataHolderSerializer.deserializeStore(db.collection("stores").document(product.storeId).get().await())
                        )
                    }
            }
            cartItems?.let { items ->
                Order(
                    items = items,
                    id = document.id,
                    address = document.get("address") as? Address ?: Address(),
                    stores = cartItems.map { it.store }.toSet().toList(),
                    total = cartItems.sumOf { it.totalPrice.toDouble() }.toFloat()
                )
            }
        }
    }

    private fun signIn(user: FirebaseUser?): String {
        return try {
            user?.uid ?: throw groupAuthExceptions(NoUidException("No UID found after sign in"))
        } catch (e: Exception) {
            throw groupAuthExceptions(e)
        }
    }

    private fun groupAuthExceptions(exception: Throwable): Throwable{
        Log.d("Auth exception", "$exception: ${exception.message}")
        throw exception
    }

    private fun groupFirestoreExceptions(exception: Throwable): Throwable{
        Log.d("Firestore exception", "$exception: ${exception.message}")
        throw exception
    }
}

