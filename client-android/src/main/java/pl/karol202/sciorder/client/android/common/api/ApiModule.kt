package pl.karol202.sciorder.client.android.common.api

import io.ktor.client.engine.okhttp.OkHttp
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import pl.karol202.sciorder.client.android.common.R
import pl.karol202.sciorder.client.common.api.KtorBasicApi
import pl.karol202.sciorder.client.common.api.admin.AdminApi
import pl.karol202.sciorder.client.common.api.admin.KtorAdminApi
import pl.karol202.sciorder.client.common.api.createApiHttpClient
import pl.karol202.sciorder.client.common.api.order.KtorOrderApi
import pl.karol202.sciorder.client.common.api.order.OrderApi
import pl.karol202.sciorder.client.common.api.product.KtorProductApi
import pl.karol202.sciorder.client.common.api.product.ProductApi
import pl.karol202.sciorder.client.common.api.store.KtorStoreApi
import pl.karol202.sciorder.client.common.api.store.StoreApi
import pl.karol202.sciorder.client.common.api.user.KtorUserApi
import pl.karol202.sciorder.client.common.api.user.UserApi

fun apiModule() = module {
	single { createApiHttpClient(OkHttp) }
	single { KtorBasicApi(get(), androidContext().getString(R.string.api_url)) }
	
	single<AdminApi> { KtorAdminApi(get()) }
	single<OrderApi> { KtorOrderApi(get()) }
	single<ProductApi> { KtorProductApi(get()) }
	single<StoreApi> { KtorStoreApi(get()) }
	single<UserApi> { KtorUserApi(get()) }
}
