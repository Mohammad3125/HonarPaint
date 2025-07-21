package ir.naderi.honarpaint.presentation.main.view.managers

import android.graphics.drawable.Drawable
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.isVisible
import com.google.android.material.checkbox.MaterialCheckBox
import ir.naderi.honarpaint.R
import ir.naderi.honarpaint.databinding.LayoutBrushPropertiesBinding
import ir.naderi.honarpaint.utils.extensions.gone
import ir.naderi.honarpaint.utils.extensions.visible
import ir.simurgh.photolib.components.paint.painters.painting.brushes.BitmapBrush
import ir.simurgh.photolib.components.paint.painters.painting.brushes.Brush
import ir.simurgh.photolib.components.paint.painters.painting.brushes.NativeBrush
import kotlin.text.toInt

class BrushExtraSettingManager(private val binding: LayoutBrushPropertiesBinding) {
    private lateinit var caretUpDrawable: Drawable
    private lateinit var caretDownDrawable: Drawable

    var onCollapse: (() -> Unit)? = null

    lateinit var globalBrush : Brush

    init {
        initialize()
    }

    private fun initialize() {
        binding.apply {
            buttonExpandBrushSettings.setOnClickListener {
                if (cardViewBrushPreviewHolder.isVisible) {
                    collapseSettings()
                    onCollapse?.invoke()
                } else {
                    expandSettings()
                }
            }

            sliderSize.apply {
                addOnChangeListener { _, value, fromUser ->
                    if (fromUser) {
                        globalBrush.size = value.toInt()
                        brushPreview.requestRender()
                    }
                }
            }
            sliderHardness.apply {
                addOnChangeListener { _, value, fromUser ->
                    if (fromUser && globalBrush is NativeBrush) {
                        (globalBrush as NativeBrush).softness = value
                        brushPreview.requestRender()
                    }
                }
            }
            sliderOpacity.apply {
                addOnChangeListener { _, value, _ ->
                    globalBrush.opacity = value
                    brushPreview.requestRender()
                }
            }
            sliderSpacing.apply {
                addOnChangeListener { _, value, fromUser ->
                    if (fromUser) {
                        globalBrush.spacing = value
                        brushPreview.requestRender()
                    }
                }
            }
            sliderScatter.apply {
                addOnChangeListener { _, value, fromUser ->
                    if (fromUser) {
                        globalBrush.scatter = value
                        brushPreview.requestRender()
                    }
                }
            }
            sliderSizeJitter.apply {
                addOnChangeListener { _, value, fromUser ->
                    if (fromUser) {
                        globalBrush.sizeJitter = value
                        brushPreview.requestRender()
                    }
                }
            }
            sliderAngle.apply {
                addOnChangeListener { _, value, fromUser ->
                    if (fromUser) {
                        globalBrush.angle = value
                        brushPreview.requestRender()
                    }
                }
            }
            sliderAngleJitter.apply {
                addOnChangeListener { _, value, fromUser ->
                    if (fromUser) {
                        globalBrush.angleJitter = value
                        brushPreview.requestRender()
                    }
                }
            }
            sliderSquish.apply {
                addOnChangeListener { _, value, fromUser ->
                    if (fromUser) {
                        globalBrush.squish = value
                        brushPreview.requestRender()
                    }
                }
            }
            sliderHueJitter.apply {
                addOnChangeListener { _, value, fromUser ->
                    if (fromUser) {
                        globalBrush.hueJitter = value.toInt()
                        brushPreview.requestRender()
                    }
                }
            }
            sliderHueFlow.apply {
                addOnChangeListener { _, value, fromUser ->
                    if (fromUser) {
                        globalBrush.hueFlow = value
                        brushPreview.requestRender()
                    }
                }
            }
            sliderHueDistance.apply {
                addOnChangeListener { _, value, fromUser ->
                    if (fromUser) {
                        globalBrush.hueDistance = value.toInt()
                        brushPreview.requestRender()
                    }
                }
            }

            sliderBrushSmoothness.apply {
                addOnChangeListener { _, value, fromUser ->
                    if (fromUser) {
                        globalBrush.smoothness = value
                        brushPreview.requestRender()
                    }
                }
            }

            sliderBrushTaperStartSize.apply {
                addOnChangeListener { _, value, fromUser ->
                    if (fromUser) {
                        globalBrush.startTaperSize = value
                        brushPreview.requestRender()
                    }
                }
            }

            sliderBrushTaperSpeed.apply {
                addOnChangeListener { _, value, fromUser ->
                    if (fromUser) {
                        globalBrush.startTaperSpeed = value
                        brushPreview.requestRender()
                    }
                }
            }


            /*
        sliderSizeVariance = findViewById<Slider>(R.id.sliderBrushSizeVariance).apply {
            addOnChangeListener { _, value, fromUser ->
                if (fromUser) {
                    globalBrush.sizeVariance = value
                    brushPreview.requestRender()
                }
            }
        }

        sliderSizeVarianceSensitivity =
            findViewById<Slider>(R.id.sliderBrushSizeVarianceSensitivity).apply {
                addOnChangeListener { _, value, fromUser ->
                    if (fromUser) {
                        globalBrush.sizeVarianceSensitivity = value
                        brushPreview.requestRender()
                    }
                }
            }

        sliderSizeVarianceEasing =
            findViewById<Slider>(R.id.sliderBrushSizeVarianceEasing).apply {
                addOnChangeListener { _, value, fromUser ->
                    if (fromUser) {
                        globalBrush.sizeVarianceEasing = value
                        brushPreview.requestRender()
                    }
                }
            }
         */
            /*

                    sliderOpacityVariance =
                        findViewById<Slider>(R.id.sliderBrushOpacityVariance).apply {
                            addOnChangeListener { _, value, fromUser ->
                                if (fromUser) {
                                    globalBrush.opacityVariance = value
                                    brushPreview.requestRender()
                                }
                            }
                        }

                    sliderOpacityVarianceSensitivity =
                        findViewById<Slider>(R.id.sliderBrushOpacityVarianceSensitivity).apply {
                            addOnChangeListener { _, value, fromUser ->
                                if (fromUser) {
                                    globalBrush.opacityVarianceSpeed = value
                                    brushPreview.requestRender()
                                }
                            }
                        }

                    sliderOpacityVarianceEasing =
                        findViewById<Slider>(R.id.sliderBrushOpacityVarianceEasing).apply {
                            addOnChangeListener { _, value, fromUser ->
                                if (fromUser) {
                                    globalBrush.opacityVarianceEasing = value
                                    brushPreview.requestRender()
                                }
                            }
                        }
        */

            sliderMinimumPressureSize.apply {
                addOnChangeListener { _, value, fromUser ->
                    if (fromUser) {
                        globalBrush.minimumPressureSize = value
                        brushPreview.requestRender()
                    }
                }
            }
            sliderMaximumPressureSize.apply {
                addOnChangeListener { _, value, fromUser ->
                    if (fromUser) {
                        globalBrush.maximumPressureSize = value
                        brushPreview.requestRender()
                    }
                }
            }
            sliderMinimumPressureOpacity.apply {
                addOnChangeListener { _, value, fromUser ->
                    if (fromUser) {
                        globalBrush.minimumPressureOpacity = value
                        brushPreview.requestRender()
                    }
                }
            }
            sliderMaximumPressureOpacity.apply {
                addOnChangeListener { _, value, fromUser ->
                    if (fromUser) {
                        globalBrush.maximumPressureOpacity = value
                        brushPreview.requestRender()
                    }
                }
            }

            checkBoxAlphaBlend.apply {
                addOnCheckedStateChangedListener { _, state ->
                    globalBrush.alphaBlend = state == MaterialCheckBox.STATE_CHECKED
                }
            }

            checkBoxSpeedSensitive.apply {
                addOnCheckedStateChangedListener { _, state ->
                    if (state == MaterialCheckBox.STATE_CHECKED) {
                        globalBrush.sizeVariance = 0.4f
                        checkBoxPressureSize.isChecked = false
                    } else {
                        globalBrush.sizeVariance = 1f
                        checkBoxPressureSize.isChecked = true
                    }
                }
            }

            checkBoxAutoRotate.apply {
                addOnCheckedStateChangedListener { _, state ->
                    globalBrush.autoRotate = state == MaterialCheckBox.STATE_CHECKED
                    brushPreview.requestRender()
                }
            }
            checkBoxPressureSize.apply {
                addOnCheckedStateChangedListener { _, state ->
                    if (state == MaterialCheckBox.STATE_CHECKED) {
                        globalBrush.isSizePressureSensitive = true
                        sliderMinimumPressureSize.isEnabled = true
                        sliderMaximumPressureSize.isEnabled = true
                    } else {
                        globalBrush.isSizePressureSensitive = false
                        sliderMinimumPressureSize.isEnabled = false
                        sliderMaximumPressureSize.isEnabled = false
                    }
                    brushPreview.requestRender()
                }
            }

            checkBoxPressureOpacity.apply {
                addOnCheckedStateChangedListener { _, state ->
                    if (state == MaterialCheckBox.STATE_CHECKED) {
                        globalBrush.isOpacityPressureSensitive = true
                        sliderMinimumPressureOpacity.isEnabled = true
                        sliderMaximumPressureOpacity.isEnabled = true
                    } else {
                        globalBrush.isSizePressureSensitive = false
                        sliderMinimumPressureOpacity.isEnabled = false
                        sliderMaximumPressureOpacity.isEnabled = false
                    }
                    brushPreview.requestRender()
                }
            }

            root.context.apply {
                caretDownDrawable = ResourcesCompat.getDrawable(
                    resources,
                    R.drawable.ic_layer_down,
                    theme
                )!!

                caretUpDrawable = ResourcesCompat.getDrawable(
                    resources,
                    R.drawable.ic_layer_up,
                    theme
                )!!
            }
        }
    }

    private fun setSliders(brush: Brush) {
        binding.apply {
            when (brush) {
                is BitmapBrush -> {
                    textViewHardness.gone()
                    sliderHardness.gone()
                }

                is NativeBrush -> {
                    textViewHardness.visible()
                    sliderHardness.visible()
                    sliderHardness.value = brush.softness
                }
            }

            sliderSize.value = brush.size.toFloat()
            sliderOpacity.value = brush.opacity
            sliderSpacing.value = brush.spacing
            sliderSizeJitter.value = brush.sizeJitter
            sliderAngle.value = brush.angle
            sliderAngleJitter.value = brush.angleJitter
            sliderSquish.value = brush.squish
            sliderScatter.value = brush.scatter
            sliderHueJitter.value = brush.hueJitter.toFloat()
            sliderHueDistance.value = brush.hueDistance.toFloat()
            sliderHueFlow.value = brush.hueFlow
            sliderBrushSmoothness.value = brush.smoothness
            sliderBrushTaperStartSize.value = brush.startTaperSize
            sliderBrushTaperSpeed.value = brush.startTaperSpeed
            checkBoxAutoRotate.isChecked = brush.autoRotate
            checkBoxAlphaBlend.isChecked = brush.alphaBlend
            checkBoxSpeedSensitive.isChecked = brush.sizeVariance != 1f
            sliderMinimumPressureSize.value = brush.minimumPressureSize
            sliderMaximumPressureSize.value = brush.maximumPressureSize
            sliderMinimumPressureOpacity.value = brush.minimumPressureOpacity
            sliderMaximumPressureOpacity.value = brush.maximumPressureOpacity
            checkBoxPressureSize.isChecked = brush.isSizePressureSensitive
            checkBoxPressureOpacity.isChecked =
                brush.isOpacityPressureSensitive
        }
    }

    fun setSlidersAndPreview(globalBrush: Brush) {
        setSliders(globalBrush)
        binding.brushPreview.brush = globalBrush
    }

    fun collapseSettings() {
        binding.apply {
            cardViewBrushPreviewHolder.gone()
            layoutBrushLayer.layoutParams.height =
                root.context.resources.getDimensionPixelSize(R.dimen.layerBrushSettingNormalHeight)
            buttonExpandBrushSettings.setImageDrawable(caretUpDrawable)
        }
    }

    fun expandSettings() {
        binding.apply {
            cardViewBrushPreviewHolder.visible()
            layoutBrushLayer.layoutParams.height =
                root.context.resources.getDimensionPixelSize(R.dimen.layerBrushSettingExpanded)
            buttonExpandBrushSettings.setImageDrawable(caretDownDrawable)
        }
    }
}