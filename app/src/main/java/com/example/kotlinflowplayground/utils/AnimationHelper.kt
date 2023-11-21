package com.example.kotlinflowplayground.utils

import android.animation.ObjectAnimator
import android.content.Context
import android.view.View
import androidx.interpolator.view.animation.FastOutLinearInInterpolator

class AnimationHelper {

    companion object {

        private var deviceWidth = 0f
        private var deviceHeight = 0f

        fun initialize(context : Context) {
            deviceWidth = context.resources.displayMetrics.widthPixels.toFloat()
            deviceHeight = context.resources.displayMetrics.heightPixels.toFloat()
        }

        fun enterFromRightAnimation(views : ArrayList<View>){
            for (view in views ) {
                val startX = deviceWidth + view.width / 2
                val endX = view.x

                ObjectAnimator.ofFloat(
                    view,
                    View.X,
                    startX,
                    endX)
                    .apply {
                        duration = 300
                        interpolator = FastOutLinearInInterpolator()
                        start()
                    }
            }
        }

        fun enterFromLeftAnimation(views : ArrayList<View>){
            for (view in views ) {
                val startX = 0 - view.x
                val endX = view.x

                ObjectAnimator.ofFloat(
                    view,
                    View.X,
                    startX,
                    endX)
                    .apply {
                        duration = 300
                        interpolator = FastOutLinearInInterpolator()
                        start()
                    }
            }
        }

        fun enterFromBottomLeft(views : ArrayList<View>) {

            for (view in views ) {

                val startX = (-view.width.toFloat())
                val startY = (deviceHeight + view.height).toFloat()
                val endX = view.x
                val endY = view.y


                ObjectAnimator.ofFloat(
                    view,
                    View.X,
                    startX,
                    endX)
                    .apply {
                        duration = 300
                        interpolator = FastOutLinearInInterpolator()
                        start()
                    }

                ObjectAnimator.ofFloat(
                    view,
                    View.Y,
                    startY,
                    endY)
                    .apply {
                        duration = 300
                        interpolator = FastOutLinearInInterpolator()
                        start()
                    }
            }
        }

        fun hideViews(views: ArrayList<View>) {
            for (view in views) {
                view.visibility = View.INVISIBLE
            }
        }

        fun showViews(views : ArrayList<View>) {
            for (view in views) {
                view.visibility = View.VISIBLE
            }
        }
    }
}