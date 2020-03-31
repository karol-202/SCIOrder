package pl.karol202.sciorder.client.android.admin.ui.fragment

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.NavHostFragment
import kotlinx.android.synthetic.main.fragment_register.*
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import pl.karol202.sciorder.client.android.admin.R
import pl.karol202.sciorder.client.android.common.component.InflatedFragment
import pl.karol202.sciorder.client.android.common.util.observeEvent
import pl.karol202.sciorder.client.android.common.util.observeNonNull
import pl.karol202.sciorder.client.android.common.util.showSnackbar
import pl.karol202.sciorder.client.android.common.viewmodel.AdminLoginAndroidViewModel
import pl.karol202.sciorder.client.common.viewmodel.AdminLoginViewModel

class RegisterFragment : InflatedFragment()
{
	private val loginViewModel by sharedViewModel<AdminLoginAndroidViewModel>()

	private val navController by lazy { NavHostFragment.findNavController(this) }

	override val layoutRes = R.layout.fragment_register

	override fun onViewCreated(view: View, savedInstanceState: Bundle?)
	{
		initRegisterButton()

		observeAdmin()
		observeError()
	}
	
	override fun onResume()
	{
		super.onResume()
		initAdminNameEditText()
		initPasswordEditText()
	}

	private fun initRegisterButton() = buttonRegister.setOnClickListener { register() }
	
	private fun initAdminNameEditText() = editTextRegisterAdminName.text?.clear()
	
	private fun initPasswordEditText() = editTextRegisterPassword.text?.clear()

	private fun observeAdmin() = loginViewModel.adminLiveData.observeNonNull(viewLifecycleOwner) { goToMainFragment() }

	private fun observeError() = loginViewModel.errorEventLiveData.observeEvent(viewLifecycleOwner) { showError(it) }
	
	private fun showError(error: AdminLoginViewModel.Error) = showSnackbar(getErrorText(error))
	
	private fun getErrorText(error: AdminLoginViewModel.Error) = when(error)
	{
		AdminLoginViewModel.Error.NETWORK -> R.string.text_network_error
		AdminLoginViewModel.Error.NAME_BUSY -> R.string.text_register_error_name_busy
		AdminLoginViewModel.Error.NAME_INVALID -> R.string.text_register_error_name_invalid
		AdminLoginViewModel.Error.PASSWORD_INVALID -> R.string.text_register_error_password_invalid
		else -> R.string.text_register_error
	}

	private fun register() = loginViewModel.register(getAdminName(), getPassword())
	
	private fun getAdminName() = editTextRegisterAdminName.text?.toString().orEmpty()
	
	private fun getPassword() = editTextRegisterPassword.text?.toString().orEmpty()

	private fun goToMainFragment() = navController.navigate(RegisterFragmentDirections.actionRegisterToMain())
}
