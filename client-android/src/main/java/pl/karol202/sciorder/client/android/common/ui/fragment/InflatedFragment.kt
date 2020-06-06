package pl.karol202.sciorder.client.android.common.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment

abstract class InflatedFragment : Fragment()
{
	abstract val layoutRes: Int

	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
			inflater.inflate(layoutRes, container, false)
}
