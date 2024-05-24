package com.example.foodboxapp.viewmodels

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.foodboxapp.backend.CartRepository
import com.example.foodboxapp.backend.Product
import com.example.foodboxapp.backend.ProductRepository
import com.example.foodboxapp.backend.Store
import com.example.foodboxapp.backend.dummyProductLists
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


data class ProductUiState(
    val loading: Boolean = false,
    val products: List<Product> = emptyList()
)
class ProductViewModel(
    private val productRepo: ProductRepository,
    private val cartRepo: CartRepository
): ViewModel() {

    val uiState = mutableStateOf(ProductUiState())
    fun loadProducts(store: Store){
        viewModelScope.launch(Dispatchers.Default) {
            productRepo.fetchProducts(store.title)
            productRepo.products.collect{
                uiState.value =
                    uiState.value.copy(products = dummyProductLists[store.title] ?: emptyList())
            }
        }

    }

    fun addProductToCart(product: Product, quantity: Int){
        viewModelScope.launch(Dispatchers.Default){
            cartRepo.addCartItem(
                CartItem(
                    product,
                    quantity,
                    product.price * quantity
                )
            )
        }
    }

}