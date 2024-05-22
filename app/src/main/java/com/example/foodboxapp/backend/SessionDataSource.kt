package com.example.foodboxapp.backend

interface SessionDataSource {
    fun login(username: String, password: String): Result<ByteArray>
    fun resumeSession()
    fun logout()
}

class SessionDataSourceImpl (): SessionDataSource {
   override fun login(username: String, password: String): Result<ByteArray> {
       return Result.success(byteArrayOf())
   }
   override fun resumeSession(){

   }
   override fun logout(){

   }
}
