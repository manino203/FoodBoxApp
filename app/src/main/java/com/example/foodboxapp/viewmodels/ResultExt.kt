package com.example.foodboxapp.viewmodels

import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext

suspend fun <T> Result<T>.onSuccessWithContext(context: CoroutineContext = Main, block: suspend (T) -> Unit): Result<T> {
    withContext(context){
        this@onSuccessWithContext.onSuccess{
            block(it)
        }
    }
    return this
}

suspend fun <T> Result<T>.onFailureWithContext(context: CoroutineContext = Main, block: suspend (Throwable) -> Unit): Result<T> {
    withContext(context){
        this@onFailureWithContext.onFailure{
            block(it)
        }
    }
    return this
}