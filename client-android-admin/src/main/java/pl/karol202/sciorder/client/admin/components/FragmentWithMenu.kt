package pl.karol202.sciorder.client.admin.components

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.fragment.app.Fragment

abstract class FragmentWithMenu : Fragment()
{
	abstract val menuRes: Int

	override fun onCreate(savedInstanceState: Bundle?)
	{
		super.onCreate(savedInstanceState)
		setHasOptionsMenu(true)
	}

	override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater)
	{
		inflater.inflate(menuRes, menu)
		super.onCreateOptionsMenu(menu, inflater)
	}

	override fun onOptionsItemSelected(item: MenuItem) =
			onMenuItemSelected(item.itemId)?.let { true } ?: super.onOptionsItemSelected(item)

	// Returns Unit if given item has been handled, null otherwise
	abstract fun onMenuItemSelected(itemId: Int): Unit?
}
