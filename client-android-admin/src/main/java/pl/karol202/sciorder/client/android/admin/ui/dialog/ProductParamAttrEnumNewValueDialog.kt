package pl.karol202.sciorder.client.android.admin.ui.dialog

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import kotlinx.android.synthetic.main.dialog_product_param_attr_enum_new_value.*
import pl.karol202.sciorder.client.android.admin.R
import pl.karol202.sciorder.client.common.components.ExtendedAlertDialog

class ProductParamAttrEnumNewValueDialog(context: Context,
                                         private val valueSetListener: (String) -> Unit) : ExtendedAlertDialog(context)
{
	@SuppressLint("InflateParams")
	private val view = LayoutInflater.from(context).inflate(R.layout.dialog_product_param_attr_enum_new_value, null)

	init
	{
		setTitle(R.string.dialog_product_edit_param_attr_enum_new_value)
		setView(view)
		setButton(BUTTON_POSITIVE, context.getString(R.string.action_enum_value_add)) { _, _ -> apply() }
	}

	private fun apply()
	{
		valueSetListener(editProductEditParamAttrEnumNewValue.text?.toString() ?: "")
	}
}
