package ir.naderi.honarpaint.presentation.main.viewmodel

import ir.naderi.honarpaint.data.brush.model.ResourceBrush
import ir.naderi.honarpaint.presentation.main.model.BrushPreview

interface BrushPreviewGenerator {
    fun generatePreview(resourceBrush: ResourceBrush, renderSize: Int, bitmapSize: Int): BrushPreview
}