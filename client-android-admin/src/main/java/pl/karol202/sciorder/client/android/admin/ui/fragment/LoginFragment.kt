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
import pl.karol202.sciorder.client.android.common.viewmodel.OwnerAndroidViewModel
import pl.karol202.sciorder.client.common.viewmodel.OwnerViewModel.Error.NAME_BUSY
import pl.karol202.sciorder.client.common.viewmodel.OwnerViewModel.Error.NOT_FOUND

class LoginFragment : InflatedFragment()
{
	private val ownerViewModel by sharedViewModel<OwnerAndroidViewModel>()

	private val navController by lazy { NavHostFragment.findNavController(this) }

	override val layoutRes = R.layout.fragment_login

	override fun onViewCreated(view: View, savedInstanceState: Bundle?)
	{
		initLoginButton()
		initRegisterButton()

		observeOwner()
		observeError()
	}

	private fun initLoginButton()
	{
		buttonLogin.setOnClickListener { login() }
	}

	private fun initRegisterButton()
	{
		buttonRegister.setOnClickListener { register() }
	}

	private fun observeOwner() =
			ownerViewModel.ownerLiveData.observeNonNull(viewLifecycleOwner) {
				clearLoginData()
				navigateToMainFragment()
			}

	private fun observeError() =
			ownerViewModel.errorEventLiveData.observeEvent(viewLifecycleOwner) {
				when(it)
				{
					NOT_FOUND -> showSnackbar(R.string.text_login_error_not_found)
					NAME_BUSY -> showSnackbar(R.string.text_login_error_name_busy)
					else -> showSnackbar(R.string.text_login_error)
				}
			}

	private fun login()
	{
		val (owner, password) = getOwnerAndPassword()
		ownerViewModel.login(owner, password)
	}

	private fun register()
	{
		val (owner, password) = getOwnerAndPassword()
		ownerViewModel.register(owner, password)
	}

	private fun getOwnerAndPassword() =
			(editTextLoginOwner.text?.toString() ?: "") to (editTextLoginPassword.text?.toString() ?: "")

	private fun clearLoginData()
	{
		editTextLoginOwner.text = null
		editTextLoginPassword.text = null
	}

	private fun navigateToMainFragment()
	{
		if(navController.currentDestination?.id != R.id.loginFragment) return
		navController.navigate(LoginFragmentDirections.actionLoginFragmentToMainFragment())
	}
}
