package com.example.foodboxapp.backend.data_sources

import android.content.SharedPreferences
import androidx.core.content.edit
import com.example.foodboxapp.backend.data_holders.Account
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json


interface AccountDataSource {

    fun load(): Account?
    fun save(account: Account?)

}


class AccountDataSourceImpl(
    private val sharedPrefs: SharedPreferences
): AccountDataSource{

    companion object{
        const val ACCOUNT_KEY = "account"
        const val ACCOUNT_PREFS_NAME = "account"
    }
    @OptIn(ExperimentalSerializationApi::class)
    override fun load(): Account? {
        return sharedPrefs.getString(ACCOUNT_KEY, null)?.let {
            Json.decodeFromString(it)
        }
         
        
    }

    @OptIn(ExperimentalSerializationApi::class)
    override fun save(account: Account?) {
        sharedPrefs.edit {
            account?.let {
                putString(ACCOUNT_KEY, Json.encodeToString(it))
            } ?: remove(ACCOUNT_KEY)
            commit()
        }
    }
}
