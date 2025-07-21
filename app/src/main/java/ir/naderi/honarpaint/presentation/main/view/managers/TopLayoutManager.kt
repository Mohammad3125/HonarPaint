package ir.naderi.honarpaint.presentation.main.view.managers

import androidx.annotation.ColorInt
import ir.naderi.honarpaint.databinding.LayoutTopMainBinding
import ir.naderi.honarpaint.utils.extensions.animateAlphaWithAppStyle
import ir.naderi.honarpaint.utils.extensions.setIconColor

class TopLayoutManager(private val binding: LayoutTopMainBinding) {

    var onLayersClicked: (() -> Unit)? = null

    var onUndoClicked: (() -> Unit)? = null

    var onRedoClicked: (() -> Unit)? = null

    var onGetBackClicked: (() -> Unit)? = null

    var onResetMatrixClicked: (() -> Unit)? = null

    var onSaveFinalImageClicked: (() -> Unit)? = null

    init {
        initialize()
    }

    private fun initialize() {
        binding.apply {
            buttonGetBack.setOnClickListener {
                onGetBackClicked?.invoke()
            }
            buttonPaintUndo.setOnClickListener {
                onUndoClicked?.invoke()
            }
            buttonPaintRedo.setOnClickListener {
                onRedoClicked?.invoke()
            }
            buttonPaintLayers.setOnClickListener {
                onLayersClicked?.invoke()
            }
            buttonResetMatrix.setOnClickListener {
                onResetMatrixClicked?.invoke()
            }
            buttonSaveFinalImage.setOnClickListener {
                onSaveFinalImageClicked?.invoke()
            }
        }
    }

    fun setUndoRedoState(isUndoEnabled: Boolean, isRedoEnabled: Boolean) {
        binding.apply {
            if (isUndoEnabled) {
                buttonPaintUndo.animateAlphaWithAppStyle(1f)
            } else {
                buttonPaintUndo.animateAlphaWithAppStyle(0.5f)
            }

            if (isRedoEnabled) {
                buttonPaintRedo.animateAlphaWithAppStyle(1f)
            } else {
                buttonPaintRedo.animateAlphaWithAppStyle(0.5f)
            }
        }
    }

    fun setButtonLayersColor(@ColorInt color: Int) {
        binding.buttonPaintLayers.setIconColor(color)
    }
}