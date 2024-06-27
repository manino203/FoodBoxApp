package com.example.foodboxapp.backend.network

import android.util.Log
import com.example.foodboxapp.backend.data_holders.Account
import com.example.foodboxapp.backend.data_holders.Address
import com.example.foodboxapp.backend.data_holders.toAccountType
import com.example.foodboxapp.backend.data_holders.toPaymentMethod
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

}

class FoodBoxServiceImpl(): FoodBoxService{

    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()


    override suspend fun fetchAccount(uid: String): Account {
        return try {
            db.collection("accounts").document(uid).get().await().let {
                Account(
                    id = uid,
                    email = it.get("email").toString(),
                    address = Address(),
                    type = it.get("type").toString().toAccountType(),
                    paymentMethod = it.get("paymentMethod").toString().toPaymentMethod()
                )
            }
        }catch (e: Exception){
            throw groupFirestoreExceptions(e)
        }
    }

    override suspend fun updateAccount(acc: Account): Account {
        try{
            db.collection("accounts").document(acc.id).set(
                object {
                    val email = acc.email
                    val address = acc.address
                    val type = acc.type.toString()
                    val paymentMethod = acc.paymentMethod.toString()
                }
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
