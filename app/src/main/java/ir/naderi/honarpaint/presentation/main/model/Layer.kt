package ir.naderi.honarpaint.presentation.main.model

import ir.simurgh.photolib.components.paint.view.PaintLayer

data class Layer(
    val id: Int,
    val paintLayer: PaintLayer,
    var isSelected: Boolean = false,
    var isChecked: Boolean = false
    )