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

	private fun initLoginButton() = buttonLogin.setOnClickListener { login() }
	
	private fun initGoToRegisterButton() = buttonGoToRegister.setOnClickListener { goToRegisterFragment() }

	private fun observeAdmin() =
			loginViewModel.adminLiveData.observeNonNull(viewLifecycleOwner) {
				clearLoginData()
				goToMainFragment()
			}

	private fun observeError() =
			loginViewModel.errorEventLiveData.observeEvent(viewLifecycleOwner) {
				when(it)
				{
					AdminLoginViewModel.Error.NETWORK -> showSnackbar(R.string.text_network_error)
					AdminLoginViewModel.Error.CANNOT_LOGIN -> showSnackbar(R.string.text_login_error_cannot_login)
					else -> showSnackbar(R.string.text_login_error)
				}
			}

	private fun login()
	{
		val adminName = editTextLoginAdminName.text?.toString() ?: ""
		val password = editTextLoginPassword.text?.toString() ?: ""
		loginViewModel.login(adminName, password)
	}

	private fun clearLoginData()
	{
		editTextLoginAdminName.text = null
		editTextLoginPassword.text = null
	}

	private fun goToMainFragment() =
			navController.navigate(LoginFragmentDirections.actionLoginFragmentToMainFragment())
	
	private fun goToRegisterFragment() =
			navController.navigate(LoginFragmentDirections.actionLoginFragmentToRegisterFragment())
}
