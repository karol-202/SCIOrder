package pl.karol202.sciorder.server.service.user

import pl.karol202.sciorder.common.request.UserLoginRequest
import pl.karol202.sciorder.common.request.UserRequest
import pl.karol202.sciorder.server.service.transaction.TransactionService

class TransactionUserService(private val delegate: UserService,
                             private val transactionService: TransactionService) : UserService
{
	override suspend fun insertUser(user: UserRequest) = transactionService.runTransaction {
		delegate.insertUser(user)
	}
	
	override suspend fun loginUser(request: UserLoginRequest) = transactionService.runTransaction {
		delegate.loginUser(request)
	}
}
