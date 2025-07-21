package ir.naderi.honarpaint.presentation.main.view.managers

import android.graphics.Color
import android.graphics.drawable.RippleDrawable
import android.view.LayoutInflater
import android.view.WindowManager
import androidx.appcompat.app.AlertDialog
import androidx.core.view.forEach
import com.google.android.material.button.MaterialButton
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.shape.MaterialShapeDrawable
import ir.kotlin.kavehcolorpicker.KavehColorPicker
import ir.naderi.honarpaint.R.layout
import ir.naderi.honarpaint.R.string
import ir.naderi.honarpaint.databinding.ColorLayoutBinding
import ir.naderi.honarpaint.databinding.LayoutColorPickerBinding
import ir.simurgh.photolib.utils.extensions.dp

class ColorLayoutManager(private val binding: ColorLayoutBinding) {

    private lateinit var kavehColorPicker: KavehColorPicker
    var onPickColor: ((Int) -> Unit)? = null

    var onDialogCancel: (() -> Unit)? = null

    var onDialogShown: (() -> Unit)? = null

    var onDropperShown: (() -> Unit)? = null

    private lateinit var colorDialog: AlertDialog
    private var lastSelectedColorCircle: MaterialButton? = null
    private var lastSelectedColor: Int = Color.BLACK

    init {
        createColorDialog()
    }

    fun initializeLayout() {
        binding.apply {
            root.context.apply {
                lastSelectedColorCircle?.strokeWidth = 0

                buttonSolidColorPicker.setOnClickListener {
                    kavehColorPicker.color = lastSelectedColor
                    onDialogShown?.invoke()
                    colorDialog.show()
                }

                buttonSolidDropper.setOnClickListener {
                   onDropperShown?.invoke()
                }

                colorCirclesHolder.forEach {
                    it.setOnClickListener { v ->
                        lastSelectedColorCircle = (v as MaterialButton).apply {
                            lastSelectedColorCircle?.strokeWidth = 0
                            val tintColor =
                                ((this.background as RippleDrawable).getDrawable(1) as MaterialShapeDrawable).resolvedTintColor
                            onPickColor?.invoke(tintColor)
                            strokeWidth = dp(2).toInt()
                            lastSelectedColor = tintColor
                        }
                    }
                }
            }
        }
    }

    private fun createColorDialog() {
        binding.apply {
            root.context.apply {
                val colorPickerDialogBuilder = MaterialAlertDialogBuilder(root.context)

                val colorPickerLayout =
                    LayoutInflater.from(this).inflate(layout.layout_color_picker, null)

                LayoutColorPickerBinding.bind(colorPickerLayout).apply {

                    kavehColorPicker = mainColorPicker
                    mainColorPicker.alphaSliderView = opacityBar
                    mainColorPicker.hueSliderView = svBar
                    mainColorPicker.color = Color.BLACK

                    colorPickerDialogBuilder.setPositiveButton(getString(string.pick)) { _, _ ->
                        lastSelectedColor = mainColorPicker.color
                        onPickColor?.invoke(lastSelectedColor)
                    }

                    colorPickerDialogBuilder.setView(colorPickerLayout)

                    colorPickerDialogBuilder.setOnDismissListener {
                        onDialogCancel?.invoke()
                    }

                    colorPickerDialogBuilder.setOnCancelListener {
                        onDialogCancel?.invoke()
                    }

                    colorDialog = colorPickerDialogBuilder.create()

                    colorDialog.window?.apply {
                        setFlags(
                            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                        )
                        setFlags(
                            WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL,
                            WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                        )
                    }

                }
            }
        }
    }

}

