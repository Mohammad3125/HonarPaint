package ir.naderi.honarpaint.utils.extensions

import com.google.android.material.slider.Slider

fun Slider.addOnSlideEnded(operation: (slider: Slider) -> Unit) {
    addOnSliderTouchListener(object : Slider.OnSliderTouchListener {
        override fun onStartTrackingTouch(slider: Slider) {

        }

        override fun onStopTrackingTouch(slider: Slider) {
            operation.invoke(this@addOnSlideEnded)
        }
    })
}