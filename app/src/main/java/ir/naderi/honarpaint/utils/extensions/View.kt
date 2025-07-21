package ir.naderi.honarpaint.utils.extensions

import android.content.res.ColorStateList
import android.view.View
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.view.isVisible


fun View.flipVisibility() {
    visibility = if (isVisible) View.GONE else View.VISIBLE
}

fun View.gone() {
    visibility = View.GONE
}

fun View.visible() {
    visibility = View.VISIBLE
}

fun View.invisible() {
    visibility = View.INVISIBLE
}

fun View.animateAlphaWithAppStyle(
    alphaValue: Float,
    duration: Long = ITEM_ANIMATION_DURATION
) {
    animate().alpha(alphaValue).setDuration(duration).start()
}

fun AppCompatImageView.setIconColor(color: Int) {
    imageTintList = ColorStateList.valueOf(color)
}

private const val ITEM_ANIMATION_DURATION = 300L
