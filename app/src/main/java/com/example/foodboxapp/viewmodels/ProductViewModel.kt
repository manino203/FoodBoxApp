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
import com.example.foodboxapp.ui.composables.UiStateError
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


data class ProductUiState(
    val loading: Boolean = true,
    val isRefreshing: Boolean = false,
    val store: Store? = null,
    val products: List<Product> = emptyList(),
    val title: String? = null,
    val error: UiStateError? = null
)

class ProductViewModel(
    private val productRepo: ProductRepository,
    private val cartRepo: CartRepository,
    private val storeRepo: StoreRepository,
    private val accountRepo: AccountRepository
): ViewModel() {

    val uiState = mutableStateOf(ProductUiState())

    fun loadProducts(storeId: String){
        viewModelScope.launch(Main) {
            productRepo.products.collectLatest { products ->
                uiState.value =
                    uiState.value.copy(products = products.filter { it.storeId == storeId })
            }
        }

        uiState.value = uiState.value.copy(loading = true)
        viewModelScope.launch(Main) {
            update(storeId)
            uiState.value = uiState.value.copy(loading = false)
        }
    }

    fun addProductToCart(product: Product, quantity: Int, storeId: String){
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
            uiState.value = uiState.value.copy(error = UiStateError(it))
        }
    }
    fun refresh(storeId: String){
        uiState.value = uiState.value.copy(loading = true, isRefreshing = true)
        viewModelScope.launch(Main) {
            update(storeId)
            uiState.value = uiState.value.copy(loading = false, isRefreshing = false)
        }
    }
    private suspend fun update(storeId: String){
        withContext(IO){
            productRepo.fetchProducts(storeId).onFailureWithContext {
                uiState.value = uiState.value.copy(error = UiStateError(it))
            }
        }
    }

}
