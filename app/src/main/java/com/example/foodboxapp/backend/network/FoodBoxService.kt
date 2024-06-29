package com.example.foodboxapp.backend.network

import android.util.Log
import com.example.foodboxapp.backend.data_holders.Account
import com.example.foodboxapp.backend.data_holders.Address
import com.example.foodboxapp.backend.data_holders.Product
import com.example.foodboxapp.backend.data_holders.Store
import com.example.foodboxapp.backend.data_holders.toAccountType
import com.example.foodboxapp.backend.data_holders.toPaymentMethod
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.DocumentSnapshot
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
}

class FoodBoxServiceImpl(): FoodBoxService{

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

interface DataHolderSerializer{
    companion object{

        private fun log(tag: String, document: DocumentSnapshot){
            Log.d(
                "DatabaseQueryResult: $tag",
                buildString {
                    append("$tag{\n")
                    document.data?.forEach{
                        append("    ${it.key} = ${it.value}\n")
                    }
                    append("}")
                }
            )

        }

        fun serializeAccount(acc: Account): Any {
            return object {
                val email = acc.email
                val address = acc.address
                val type = acc.type.toString()
                val paymentMethod = acc.paymentMethod.toString()
            }
        }

        fun deserializeAccount(uid: String, documentSnapshot: DocumentSnapshot): Account {
            log("Account", documentSnapshot)
            return Account(
                id = uid,
                email = documentSnapshot.get("email").toString(),
                address = documentSnapshot.get("address") as? Address ?: Address(),
                type = documentSnapshot.get("type").toString().toAccountType(),
                paymentMethod = documentSnapshot.get("paymentMethod").toString().toPaymentMethod()
            )
        }


        fun deserializeStore(documentSnapshot: DocumentSnapshot): Store {
            log("Store", documentSnapshot)
            return Store(
                title = documentSnapshot.get("title").toString(),
                address = documentSnapshot.get("address") as? Address,
                imageUrl = documentSnapshot.get("title").toString(),
                id = documentSnapshot.id
            )
        }


        fun deserializeProduct(documentSnapshot: DocumentSnapshot): Product? {
            log("Product", documentSnapshot)
            return documentSnapshot.getDouble("price")?.toFloat()?.let { price ->
                Product(
                    id = documentSnapshot.id,
                    storeId = documentSnapshot.get("storeId").toString(),
                    title = documentSnapshot.get("title").toString(),
                    imageUrl = documentSnapshot.get("imageUrl").toString(),
                    price = price,
                    details = documentSnapshot.get("details").toString(),
                )
            }
        }
    }
}

