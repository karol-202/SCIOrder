package pl.karol202.sciorder.server.service.admin

import pl.karol202.sciorder.common.request.AdminLoginRequest
import pl.karol202.sciorder.common.request.AdminRequest
import pl.karol202.sciorder.server.service.transaction.TransactionService
import pl.karol202.sciorder.server.service.transaction.invoke

class TransactionAdminService(private val delegate: AdminService,
                              private val transactionService: TransactionService) : AdminService
{
	override suspend fun insertAdmin(admin: AdminRequest) = transactionService {
		delegate.insertAdmin(admin)
	}
	
	override suspend fun deleteAdmin(adminId: Long) = transactionService {
		delegate.deleteAdmin(adminId)
	}
	
	override suspend fun loginAdmin(request: AdminLoginRequest) = transactionService {
		delegate.loginAdmin(request)
	}
}
