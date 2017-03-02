@file:Suppress("unused")

package com.github.patrickds.androidexperimental.application

import android.app.Activity
import android.support.design.widget.Snackbar
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast

inline fun consume(action: () -> Unit): Boolean {
    action()
    return true
}

fun View.appear() {
    visibility = View.VISIBLE
}

fun View.vanish() {
    visibility = View.GONE
}

fun View.enable() {
    isEnabled = true
}

fun View.disable() {
    isEnabled = false
}

fun Activity.showLongSnack(message: String){
    this.findViewById(android.R.id.content).showLongSnack(message)
}

fun Activity.showShortSnack(message: String){
    this.findViewById(android.R.id.content).showShortSnack(message)
}

fun View.showLongSnack(message: String) {
    Snackbar.make(this, message, Snackbar.LENGTH_LONG).show()
}

fun View.showLongSnack(messageResource: Int) {
    val message = context.getString(messageResource)
    Snackbar.make(this, message, Snackbar.LENGTH_LONG).show()
}

fun View.showShortSnack(message: String) {
    Snackbar.make(this, message, Snackbar.LENGTH_SHORT).show()
}

fun ViewGroup.inflate(resource: Int, attachToParent: Boolean = false): View {
    return LayoutInflater.from(context).inflate(resource, this, attachToParent)
}

fun Activity.showShortToast(message: String) =
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()