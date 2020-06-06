package pl.karol202.sciorder.client.android.common.util

import android.content.Context
import android.transition.TransitionInflater
import androidx.annotation.ColorRes
import androidx.annotation.TransitionRes
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat

fun Context.getColorCompat(@ColorRes color: Int) = ContextCompat.getColor(this, color)

fun Context.inflateTransition(@TransitionRes transition: Int) = TransitionInflater.from(this).inflateTransition(transition)

fun Context.alertDialog(builder: AlertDialog.Builder.() -> Unit) = AlertDialog.Builder(this).apply(builder)
