package ir.naderi.honarpaint.presentation.main.model

import android.graphics.Bitmap
import androidx.annotation.DrawableRes
import ir.simurgh.photolib.components.paint.painters.painting.brushes.Brush

data class BrushPreview(
    val brush: Brush,
    val brushBitmap: Bitmap,
    @DrawableRes val drawableId: List<Int>? = null
)