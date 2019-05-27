package pl.karol202.sciorder.client.admin.ui.fragment

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.NavHostFragment
import kotlinx.android.synthetic.main.fragment_login.*
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import pl.karol202.sciorder.client.admin.R
import pl.karol202.sciorder.client.admin.viewmodel.OwnerViewModel
import pl.karol202.sciorder.client.common.components.InflatedFragment
import pl.karol202.sciorder.client.common.extensions.observeEvent
import pl.karol202.sciorder.client.common.extensions.observeNonNull
import pl.karol202.sciorder.client.common.extensions.showSnackbar

class LoginFragment : InflatedFragment()
{
	private val ownerViewModel by sharedViewModel<OwnerViewModel>()

	private val navController by lazy { NavHostFragment.findNavController(this) }

	override val layoutRes = R.layout.fragment_login

	override fun onViewCreated(view: View, savedInstanceState: Bundle?)
	{
		initLoginButton()
		initRegisterButton()

		observeOwnerId()
		observeError()
	}

	private fun initLoginButton()
	{
		buttonLogin.setOnClickListener { login() }
	}

	private fun initRegisterButton()
	{
		buttonLogin.setOnClickListener { register() }
	}

	private fun observeOwnerId() =
			ownerViewModel.ownerIdSettingLiveData.observeNonNull(viewLifecycleOwner) { navigateToMainFragment() }

	private fun observeError() =
			ownerViewModel.errorEventLiveData.observeEvent(viewLifecycleOwner) {
				if(it == OwnerViewModel.Error.NAME_BUSY) showSnackbar(R.string.text_login_error_name_busy)
				else showSnackbar(R.string.text_login_error)
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

	private fun navigateToMainFragment()
	{
		if(navController.currentDestination?.id != R.id.loginFragment) return
		navController.navigate(LoginFragmentDirections.actionLoginFragmentToMainFragment())
	}
}
