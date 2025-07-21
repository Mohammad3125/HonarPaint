package ir.naderi.honarpaint.presentation.main.view.managers

import android.view.View
import ir.naderi.honarpaint.R
import ir.naderi.honarpaint.databinding.LayoutPaintToolBinding
import ir.naderi.honarpaint.utils.extensions.animateAlphaWithAppStyle

class PaintToolsLayerManager(private val binding: LayoutPaintToolBinding) {

    private var lastSelectedTool: View = binding.buttonBrushSettings

    var onBrushButtonSelected: (() -> Unit)? = null

    var onEraserButtonSelected: (() -> Unit)? = null

    var onLassoButtonSelected: (() -> Unit)? = null

    var onColorButtonSelected: (() -> Unit)? = null

    var onFillButtonSelected: (() -> Unit)? = null

    init {

        binding.apply {
            root.context.apply {
                buttonBrushSettings.setOnClickListener {
                    textViewBrushToolInPaintLayout.text = getString(R.string.brushesSelected)
                    textViewEraserPaintTool.text = getString(R.string.eraser)

                    lastSelectedTool.animateAlphaWithAppStyle(0.5f)
                    lastSelectedTool = it
                    it.animateAlphaWithAppStyle(1f)

                    onBrushButtonSelected?.invoke()

                    buttonColorPicker.animateAlphaWithAppStyle(0.5f)
                }

                buttonLassoColoring.setOnClickListener {

                    if (lastSelectedTool === it) {
                        return@setOnClickListener
                    }

                    textViewBrushToolInPaintLayout.text = getString(R.string.brushes)
                    textViewEraserPaintTool.text = getString(R.string.eraser)

                    buttonBrushSettings.animateAlphaWithAppStyle(0.5f)
                    lastSelectedTool.animateAlphaWithAppStyle(0.5f)
                    lastSelectedTool = it
                    it.animateAlphaWithAppStyle(1f)

                    onLassoButtonSelected?.invoke()
                }

                buttonBrushEraser.setOnClickListener {
                    textViewBrushToolInPaintLayout.text = getString(R.string.brushes)
                    textViewEraserPaintTool.text = getString(R.string.eraserSelected)

                    lastSelectedTool.animateAlphaWithAppStyle(0.5f)
                    lastSelectedTool = it
                    it.animateAlphaWithAppStyle(1f)

                    onEraserButtonSelected?.invoke()

                    buttonColorPicker.animateAlphaWithAppStyle(0.5f)
                }

                buttonColorPicker.setOnClickListener {
                    if (it.alpha != 1f) {
                        textViewBrushToolInPaintLayout.text = getString(R.string.brushes)
                        textViewEraserPaintTool.text = getString(R.string.eraser)

                        it.animateAlphaWithAppStyle(1f)

                        onColorButtonSelected?.invoke()

                        return@setOnClickListener
                    }

                    it.animateAlphaWithAppStyle(0.5f)

                    onColorButtonSelected?.invoke()
                }

                buttonFillColor.setOnClickListener {
                    if (lastSelectedTool === it) {
                        return@setOnClickListener
                    }

                    textViewBrushToolInPaintLayout.text = getString(R.string.brushes)
                    textViewEraserPaintTool.text = getString(R.string.eraser)

                    buttonColorPicker.animateAlphaWithAppStyle(0.5f)
                    lastSelectedTool.animateAlphaWithAppStyle(0.5f)
                    lastSelectedTool = it
                    it.animateAlphaWithAppStyle(1f)
                    onFillButtonSelected?.invoke()
                }
            }
        }
    }
}