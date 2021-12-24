package com.praveen.basicimageeditor.utils

import android.content.res.ColorStateList
import android.graphics.Color
import android.util.Log
import android.graphics.drawable.GradientDrawable
import android.view.View
import com.google.android.material.button.MaterialButton
import com.praveen.basicimageeditor.R
import java.util.*


class CommonUtils {
    companion object {
        private const val mTag = "CommonUtils"

        fun setBgColorCornerRadius(view: View, color: Int) {
            try {
                view.setBackgroundResource(R.drawable.bg_corner_radius)
                val drawable = view.background as GradientDrawable
                drawable.setColor(color)
            } catch (e: Exception) {
                Log.d(mTag, "cornerRadius:: $mTag")
            }
        }

        fun setRandomColorForMaterialButton(view: MaterialButton): Int {
            val rnd = Random()
            val color: Int = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256))
            view.backgroundTintList = ColorStateList.valueOf(color)
            return color
        }
    }
}