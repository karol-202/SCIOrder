package pl.karol202.sciorder.client.android.admin.ui.fragment

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.NavHostFragment
import kotlinx.android.synthetic.main.fragment_login.*
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import pl.karol202.sciorder.client.android.admin.R
import pl.karol202.sciorder.client.android.common.component.InflatedFragment
import pl.karol202.sciorder.client.android.common.util.observeEvent
import pl.karol202.sciorder.client.android.common.util.observeNonNull
import pl.karol202.sciorder.client.android.common.util.showSnackbar
import pl.karol202.sciorder.client.android.common.viewmodel.AdminLoginAndroidViewModel
import pl.karol202.sciorder.client.common.viewmodel.AdminLoginViewModel

class LoginFragment : InflatedFragment()
{
	private val loginViewModel by sharedViewModel<AdminLoginAndroidViewModel>()

	private val navController by lazy { NavHostFragment.findNavController(this) }

	override val layoutRes = R.layout.fragment_login

	override fun onViewCreated(view: View, savedInstanceState: Bundle?)
	{
		initLoginButton()
		initGoToRegisterButton()

		observeAdmin()
		observeError()
	}
	
	override fun onResume()
	{
		super.onResume()
		initAdminNameEditText()
		initPasswordEditText()
	}
	
	private fun initLoginButton() = buttonLogin.setOnClickListener { login() }
	
	private fun initGoToRegisterButton() = buttonGoToRegister.setOnClickListener { goToRegisterFragment() }
	
	private fun initAdminNameEditText() = editTextLoginAdminName.text?.clear()
	
	private fun initPasswordEditText() = editTextLoginPassword.text?.clear()
	
	private fun observeAdmin() = loginViewModel.adminLiveData.observeNonNull(viewLifecycleOwner) { goToMainFragment() }
	
	private fun observeError() = loginViewModel.errorEventLiveData.observeEvent(viewLifecycleOwner) { showError(it) }
	
	private fun showError(error: AdminLoginViewModel.Error) = showSnackbar(getErrorText(error))
	
	private fun getErrorText(error: AdminLoginViewModel.Error) = when(error)
	{
		AdminLoginViewModel.Error.NETWORK -> R.string.text_network_error
		AdminLoginViewModel.Error.CANNOT_LOGIN -> R.string.text_login_error_cannot_login
		else -> R.string.text_login_error
	}

	private fun login() = loginViewModel.login(getAdminName(), getPassword())
	
	private fun getAdminName() = editTextLoginAdminName.text?.toString().orEmpty()
	
	private fun getPassword() = editTextLoginPassword.text?.toString().orEmpty()

	private fun goToMainFragment() = navController.navigate(LoginFragmentDirections.actionLoginToStores())
	
	private fun goToRegisterFragment() = navController.navigate(LoginFragmentDirections.actionLoginToRegister())
}
