package com.praveen.basicimageeditor.animation

import android.content.Context
import android.util.Log
import android.view.View
import android.view.animation.AlphaAnimation
import android.view.animation.RotateAnimation

class CommonAnimation {

    companion object {
        private var mTag = "CommonAnimation"

        fun setAlphaAnimation(view: View, alphaFrom: Float, alphaTo: Float, duration: Long) {
            try {
                val alphaAnimation = AlphaAnimation(alphaFrom, alphaTo)
                alphaAnimation.duration = duration
                alphaAnimation.fillAfter = true
                view.startAnimation(alphaAnimation)
            } catch (e: Exception) {
                Log.d(mTag, "setAlphaAnimation:: " + e.printStackTrace())
            }
        }

        fun rotationAnimation(view: View,xDeg:Float,yDeg:Float,duration: Long){
            try {
                val rotateAnimation = RotateAnimation(xDeg,yDeg,RotateAnimation.RELATIVE_TO_SELF,0.5f,
                RotateAnimation.RELATIVE_TO_SELF,0.5f)
                rotateAnimation.duration = duration
                view.startAnimation(rotateAnimation)
            }catch (e:Exception){
                Log.d(mTag, "rotationAnimation:: " + e.printStackTrace())
            }
        }
    }
}