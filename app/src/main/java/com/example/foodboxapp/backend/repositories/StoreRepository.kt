package com.example.foodboxapp.backend.repositories

import com.example.foodboxapp.backend.data_sources.StoreDataSource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class Store(
    val image: String,
    val address: String,
    val title: String
)

val dummyStoreList = listOf(
    Store(
        image = "https://external-content.duckduckgo.com/iu/?u=https%3A%2F%2Ftse2.mm.bing.net%2Fth%3Fid%3DOIP.1XesvbulmFPQrA9mx-jLEgHaD4%26pid%3DApi&f=1&ipt=d15dff5777161954d2eda3842583a74b91cda7611d241948ba1ff416a2c07bfd&ipo=images",
        address = "Odbojárov 5060/33, 955 01 Topoľčany, Topoľčany District, Slovakia",
        title = "Tesco"
    ),
    Store(
        image = "https://external-content.duckduckgo.com/iu/?u=https%3A%2F%2Ftse1.mm.bing.net%2Fth%3Fid%3DOIP.kCXCdotgcGpYAuHlwoNLOgHaDK%26pid%3DApi&f=1&ipt=b8a2f49baa06a53538c8d1aea0b03f5c1cc1cc2218f0a876494242aff2344787&ipo=images",
        address = "Odbojárov 5060/33, 955 01 Topoľčany, Topoľčany District, Slovakia",
        title = "Billa"
    ),
    Store(
        image = "https://external-content.duckduckgo.com/iu/?u=https%3A%2F%2Flogos-download.com%2Fwp-content%2Fuploads%2F2016%2F03%2FLidl_logo.png&f=1&nofb=1&ipt=ad66bf2d78c91ad12af50a36af28d42ef1ab89570ad13777dc5798197ed162be&ipo=images",
        address = "Odbojárov 5060/33, 955 01 Topoľčany, Topoľčany District, Slovakia",
        title = "Lidl"
    ),
    Store(
        image = "https://external-content.duckduckgo.com/iu/?u=https%3A%2F%2Flogos-download.com%2Fwp-content%2Fuploads%2F2016%2F12%2FKaufland_logo_square.png&f=1&nofb=1&ipt=d1a577634f73628145eff640623d8a966e622cac743d6fae8f897467fc92af90&ipo=images",
        address = "Odbojárov 5060/33, 955 01 Topoľčany, Topoľčany District, Slovakia",
        title = "Kaufland"
    )

)

interface StoreRepository{

    val stores: StateFlow<List<Store>>
    suspend fun fetchStores()
}


class StoreRepositoryImpl(
    private val dataSource: StoreDataSource
): StoreRepository {
    private val _stores = MutableStateFlow(emptyList<Store>())
    override val stores: StateFlow<List<Store>>
        get() = _stores.asStateFlow()
    override suspend fun fetchStores() {
        dataSource.fetchStores().onSuccess { stores ->
            _stores.update {
                stores
            }
        }
    }
}