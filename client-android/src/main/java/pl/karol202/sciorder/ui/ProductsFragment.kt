package pl.karol202.sciorder.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_products.*
import pl.karol202.sciorder.R

class ProductsFragment : Fragment()
{
	private val adapter = ProductAdapter()

	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
			inflater.inflate(R.layout.fragment_products, container, false)

	override fun onViewCreated(view: View, savedInstanceState: Bundle?)
	{
		initRecycler()
	}

	private fun initRecycler()
	{
		recyclerProducts.layoutManager = LinearLayoutManager(context)
		recyclerProducts.adapter = adapter
		recyclerProducts.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
	}
}