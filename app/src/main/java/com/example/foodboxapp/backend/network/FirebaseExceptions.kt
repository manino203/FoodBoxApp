package com.example.foodboxapp.backend.network

class NoUidException(message: String): Exception(message){
    override fun getLocalizedMessage(): String? {
        return super.getLocalizedMessage()
    }
}
class DocumentNotFoundException(message: String): Exception(message){
    override fun getLocalizedMessage(): String? {
        return super.getLocalizedMessage()
    }
}
