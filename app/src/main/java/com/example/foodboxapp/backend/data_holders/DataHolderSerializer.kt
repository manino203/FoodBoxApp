package com.example.foodboxapp.backend.data_holders

import android.util.Log
import com.google.firebase.firestore.DocumentSnapshot

interface DataHolderSerializer{
    companion object{

        fun log(tag: String, document: DocumentSnapshot){
            Log.d(
                "Deserializing: $tag",
                buildString {
                    append("$tag{\n")
                    document.data?.forEach {
                        append("    ${it.key} = ${it.value}\n")
                    }
                    append("}")
                }
            )

        }

        fun serializeAccount(acc: Account): Map<String, *> {
            return mapOf (
                Pair("email", acc.email),
                Pair("address", acc.address),
                Pair("type", acc.type.toString()),
                Pair("paymentMethod", acc.paymentMethod.toString()),
            )
        }

        fun deserializeAccount(uid: String, documentSnapshot: DocumentSnapshot): Account {
            log("Account", documentSnapshot)
            Log.d("address", "${documentSnapshot.get("address") as? Address ?: Address()}")
            return Account(
                id = uid,
                email = documentSnapshot.get("email").toString(),
                address = Address.fromMap(documentSnapshot.get("address")),
                type = documentSnapshot.get("type").toString().toAccountType(),
                paymentMethod = documentSnapshot.get("paymentMethod").toString().toPaymentMethod()
            )
        }


        fun deserializeStore(documentSnapshot: DocumentSnapshot): Store {
            log("Store", documentSnapshot)
            return Store(
                title = documentSnapshot.get("title").toString(),
                address = documentSnapshot.get("address") as? Address,
                imageUrl = documentSnapshot.get("imageUrl").toString(),
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

        private fun serializeCartItem(item: CartItem): Map<String, *>{
            return mapOf(
                Pair("productId", item.product.id),
                Pair("count", item.count)
            )
        }

        private suspend fun deserializeCartItem(item: Map<String, *>,  getProduct: suspend (String) -> Product?, getStore: suspend (String) -> Store): CartItem?{
            return (item["count"] as? Int)?.let { count ->
                getProduct(item["productId"].toString())?.let {
                    CartItem(
                        product = it,
                        count = count,
                        store = getStore(it.storeId),
                        totalPrice = it.price * count
                    )
                }
            }
        }


        fun serializeOrder(order: Order): Map<String, *>{
            return mapOf(
                Pair("items", order.items.map { serializeCartItem(it) }),
                Pair("address", order.address),
                Pair("workerId", null)
            )
        }
    }
}