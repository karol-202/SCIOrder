package pl.karol202.sciorder.server.service.user

import pl.karol202.sciorder.common.request.UserLoginRequest
import pl.karol202.sciorder.common.request.UserRequest
import pl.karol202.sciorder.server.service.transaction.TransactionService
import pl.karol202.sciorder.server.service.transaction.invoke

class TransactionUserService(private val delegate: UserService,
                             private val transactionService: TransactionService) : UserService
{
	override suspend fun insertUser(user: UserRequest) = transactionService {
		delegate.insertUser(user)
	}
	
	override suspend fun loginUser(request: UserLoginRequest) = transactionService {
		delegate.loginUser(request)
	}
}
