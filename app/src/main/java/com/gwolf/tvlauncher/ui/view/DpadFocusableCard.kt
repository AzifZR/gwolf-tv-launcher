package com.gwolf.tvlauncher.ui.view

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Rect
import android.util.AttributeSet
import android.view.animation.DecelerateInterpolator
import android.widget.FrameLayout
import androidx.core.content.ContextCompat

class DpadFocusableCard @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private var scaleAnimator: ValueAnimator? = null

    init {
        isFocusable = true
        isFocusableInTouchMode = true
        applyBackground(false)
    }

    override fun onFocusChanged(gainFocus: Boolean, direction: Int, previouslyFocusedRect: Rect?) {
        super.onFocusChanged(gainFocus, direction, previouslyFocusedRect)
        animateScale(gainFocus)
        applyBackground(gainFocus)
        if (gainFocus) {
            elevation = 12f
            bringToFront()
        } else {
            elevation = 0f
        }
    }

    private fun animateScale(focused: Boolean) {
        val targetScale = if (focused) 1.06f else 1.0f
        scaleAnimator?.cancel()
        scaleAnimator = ValueAnimator.ofFloat(scaleX, targetScale).apply {
            duration = ANIMATION_DURATION_MS
            interpolator = DecelerateInterpolator()
            addUpdateListener { animator ->
                val value = animator.animatedValue as Float
                scaleX = value
                scaleY = value
            }
            start()
        }
    }

    private fun applyBackground(focused: Boolean) {
        val resName = if (focused) "bg_card_focus" else "bg_card_normal"
        val resId = context.resources.getIdentifier(resName, "drawable", context.packageName)
        if (resId != 0) {
            background = ContextCompat.getDrawable(context, resId)
        }
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        scaleAnimator?.cancel()
    }

    companion object {
        private const val ANIMATION_DURATION_MS = 120L
    }
}
