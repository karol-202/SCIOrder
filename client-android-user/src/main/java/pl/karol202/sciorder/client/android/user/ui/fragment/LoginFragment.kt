package pl.karol202.sciorder.client.android.user.ui.fragment

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.NavHostFragment
import kotlinx.android.synthetic.main.fragment_login.*
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import pl.karol202.sciorder.client.android.common.ui.fragment.InflatedFragment
import pl.karol202.sciorder.client.android.common.util.showSnackbar
import pl.karol202.sciorder.client.android.user.R
import pl.karol202.sciorder.client.common.viewmodel.OwnerViewModel

class LoginFragment : InflatedFragment()
{
	private val ownerViewModel by sharedViewModel<OwnerAndroidViewModel>()

	private val navController by lazy { NavHostFragment.findNavController(this) }

	override val layoutRes = R.layout.fragment_login

	override fun onViewCreated(view: View, savedInstanceState: Bundle?)
	{
		initLoginButton()

		observeOwner()
		observeError()
	}

	private fun initLoginButton()
	{
		buttonLogin.setOnClickListener { login() }
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
					OwnerViewModel.Error.NOT_FOUND -> showSnackbar(R.string.text_login_error_not_found)
					else -> showSnackbar(R.string.text_login_error)
				}
			}

	private fun login()
	{
		val owner = editTextLoginOwner.text?.toString() ?: ""
		ownerViewModel.login(owner)
	}

	private fun clearLoginData()
	{
		editTextLoginOwner.text = null
	}

	private fun navigateToMainFragment()
	{
		if(navController.currentDestination?.id != R.id.loginFragment) return
		navController.navigate(LoginFragmentDirections.actionLoginFragmentToMainFragment())
	}
}
