package ir.naderi.honarpaint.presentation.main.view.managers

import android.view.WindowManager
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.appcompat.app.AlertDialog
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import ir.naderi.honarpaint.R
import ir.naderi.honarpaint.databinding.LayoutLayerSettingBinding
import ir.naderi.honarpaint.utils.extensions.addOnSlideEnded
import ir.simurgh.photolib.components.paint.view.PaintLayer

class LayerSettingManager(private val binding: LayoutLayerSettingBinding) {
    private lateinit var layerSettingDialog: AlertDialog

    var onSliderValueChanged: ((Float) -> Unit)? = null

    var onSliderValueEnded: ((Float) -> Unit)? = null

    var onBlendingModeItemClicked: ((String) -> Unit)? = null

    init {
        initialize()
    }

    private fun initialize() {
        binding.apply {
            val layerSettingDialogBuilder = MaterialAlertDialogBuilder(root.context)

            sliderLayoutOpacity.apply {
                addOnSlideEnded { slider ->
                    onSliderValueEnded?.invoke(slider.value)
                }


                addOnChangeListener { _, value, fromUser ->
                    if (fromUser) {
                        onSliderValueChanged?.invoke(value)
                    }
                }
            }

            buttonDoneLayerSetting.setOnClickListener {
                layerSettingDialog.dismiss()
            }

            val items =
                listOf("NORMAL", "SCREEN", "MULTIPLY", "DARKEN", "LIGHTEN", "OVERLAY", "ADD")
            val adapter = ArrayAdapter(root.context, R.layout.auto_complete_list, items)
            (blendingModeListLayout.editText as? AutoCompleteTextView)?.apply {

                setAdapter(adapter)

                setOnItemClickListener { _, _, position, _ ->
                    val selectedItem = items[position]

                    onBlendingModeItemClicked?.invoke(selectedItem)
                }
            }

            layerSettingDialogBuilder.setView(root)

            layerSettingDialog = layerSettingDialogBuilder.create()

            layerSettingDialog.window?.apply {
                setFlags(
                    WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                    WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                )
                setFlags(
                    WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH,
                    WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH
                )
            }
        }
    }

    fun changeLayerSettingValues(layer: PaintLayer) {
        binding.apply {
            sliderLayoutOpacity.value = (100f * layer.opacity)

            var finalBlendingMode = layer.blendingMode.toString()

            if (finalBlendingMode == "SRC") {
                finalBlendingMode = "NORMAL"
            }

            (blendingModeListLayout.editText as? AutoCompleteTextView)?.setText(
                finalBlendingMode,
                false
            )
        }
    }

    fun show() {
        layerSettingDialog.show()
    }
}

