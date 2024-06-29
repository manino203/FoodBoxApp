package com.example.foodboxapp.viewmodels

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.foodboxapp.backend.data_holders.CartItem
import com.example.foodboxapp.backend.data_holders.Product
import com.example.foodboxapp.backend.data_holders.Store
import com.example.foodboxapp.backend.repositories.AccountRepository
import com.example.foodboxapp.backend.repositories.CartRepository
import com.example.foodboxapp.backend.repositories.ProductRepository
import com.example.foodboxapp.backend.repositories.StoreRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


data class ProductUiState(
    val loading: Boolean = true,
    val store: Store? = null,
    val products: List<Product> = emptyList(),
    val title: String? = null,

    )
class ProductViewModel(
    private val productRepo: ProductRepository,
    private val cartRepo: CartRepository,
    private val storeRepo: StoreRepository,
    private val accountRepo: AccountRepository
): ViewModel() {

    val uiState = mutableStateOf(ProductUiState())

    fun loadProducts(storeId: String){
        uiState.value = uiState.value.copy(loading = true)
        viewModelScope.launch(Dispatchers.IO) {
            productRepo.products.collect {
                uiState.value =
                    uiState.value.copy(products = it)
            }
        }

        viewModelScope.launch(Dispatchers.Default) {
            productRepo.fetchProducts(storeId)
        }.invokeOnCompletion {
            uiState.value = uiState.value.copy(loading = false)
        }
    }

    fun addProductToCart(product: Product, quantity: Int, storeId: String){
        viewModelScope.launch(Dispatchers.Default){
            storeRepo.getStore(storeId).onSuccess{ store ->
                accountRepo.account.value?.id?.let {
                    cartRepo.addCartItem(
                        CartItem(
                            product,
                            quantity,
                            store,
                            product.price * quantity
                        ),
                        it
                    )
                }
            }.onFailure {
                //todo
            }
        }
    }

}