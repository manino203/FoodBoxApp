package com.example.foodboxapp.backend.data_sources

import com.example.foodboxapp.backend.data_holders.Account
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.tasks.await

interface SessionDataSource {
    suspend fun login(email: String, password: String): Result<Account>
    suspend fun register(email: String, password: String): Result<Account>
    suspend fun resumeSession(): Result<Account>
    fun logout()
}

class SessionDataSourceImpl(
    private val accountDataSource: AccountDataSource,
): SessionDataSource {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    override suspend fun login(email: String, password: String): Result<Account> {
        return signIn{
            auth.signInWithEmailAndPassword(email, password).await().user
        }
    }

    override suspend fun register(email: String, password: String): Result<Account> {
        return signIn{
            auth.createUserWithEmailAndPassword(email, password).await().user
        }
    }

    private suspend fun signIn(authFunction: suspend () -> FirebaseUser?): Result<Account>{
        return authFunction.invoke()?.let{ user ->
                Result.success(
                    accountDataSource.fetch(user.uid)
                )
            }?: Result.failure(Exception("User doesn't exist"))


    }

    override suspend fun resumeSession(): Result<Account>{
        return signIn {
            auth.currentUser
        }
    }
    override fun logout(){
        auth.signOut()
    }
}



