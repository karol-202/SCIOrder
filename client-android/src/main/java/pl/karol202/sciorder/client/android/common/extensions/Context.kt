package pl.karol202.sciorder.client.android.common.extensions

import android.content.Context
import androidx.annotation.ColorRes
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat

fun Context.getColorCompat(@ColorRes color: Int) = ContextCompat.getColor(this, color)

fun Context.alertDialog(builder: AlertDialog.Builder.() -> Unit) = AlertDialog.Builder(this).apply(builder)
