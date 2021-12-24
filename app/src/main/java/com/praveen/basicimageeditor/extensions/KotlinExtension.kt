package com.praveen.basicimageeditor.extensions

import android.content.Context
import android.view.View
import android.widget.Toast

object KotlinExtension {
    fun Context.toast(text: String?) {
        Toast.makeText(this, text, Toast.LENGTH_LONG).show()
    }

    fun View.visible(isVisible: Boolean) {
        visibility = if (isVisible) View.VISIBLE else View.GONE
    }
}