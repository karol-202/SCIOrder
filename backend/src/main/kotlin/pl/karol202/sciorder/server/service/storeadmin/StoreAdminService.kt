package pl.karol202.sciorder.server.service.storeadmin

interface StoreAdminService
{
	suspend fun insertStoreAdminJoin(adminId: Long, storeId: Long)
}
