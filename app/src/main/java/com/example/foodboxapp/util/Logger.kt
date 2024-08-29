package com.example.foodboxapp.util

import android.util.Log
import com.google.firebase.crashlytics.FirebaseCrashlytics

interface Logger{
    companion object{
        fun logDebug(tag: String, message: String){
            Log.d(tag, message)
        }

        fun logError(tag: String, error: Exception){
            Log.e(tag, "$error")
            FirebaseCrashlytics.getInstance().recordException(error)
        }
    }
}

