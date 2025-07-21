package ir.naderi.honarpaint.data.brush.source

import ir.naderi.honarpaint.R
import ir.naderi.honarpaint.data.brush.model.ResourceBrush
import ir.simurgh.photolib.components.paint.painters.painting.brushes.BitmapBrush
import ir.simurgh.photolib.components.paint.painters.painting.brushes.NativeBrush
import ir.simurgh.photolib.components.paint.painters.painting.brushes.SpriteBrush

class DefaultResourceDataSource : ResourceDataSource {
    override suspend fun getBrushes(): List<ResourceBrush> {
        return listOf(
            ResourceBrush(NativeBrush(size = 40, softness = 0.6f), null),

            ResourceBrush(
                NativeBrush(
                    size = 40,
                    spacing = 0.1f,
                    brushShape = NativeBrush.BrushShape.RECT
                ), null
            ),

            ResourceBrush(
                NativeBrush(
                    size = 40,
                    softness = 0.05f,
                    spacing = 0.1f
                ), null
            ),

            ResourceBrush(NativeBrush(size = 40, softness = 0.8f), null),

            ResourceBrush(
                NativeBrush(
                    size = 40,
                    softness = 0.05f,
                    spacing = 0.1f
                ), null
            ),

            ResourceBrush(
                NativeBrush(
                    size = 40,
                    softness = 0.6f,
                    spacing = 0.05f,
                    angle = 60f,
                    squish = 0.5f
                ), null
            ),

            ResourceBrush(
                NativeBrush(
                    size = 20,
                    softness = 0.5f,
                    scatter = 1.15f,
                    sizeJitter = 0.8f,
                    opacity = 1f,
                    spacing = 0.16f,
                    angleJitter = 1f,
                    squish = 0.7f
                ), null
            ),

            ResourceBrush(
                BitmapBrush(
                    size = 50,
                    angleJitter = 0.5f,
                    sizeJitter = 0.3f,
                    spacing = 0.24f,
                    scatter = 0.8f
                ), listOf(ir.simurgh.photolib.R.drawable.hair)
            ),

            ResourceBrush(
                BitmapBrush(
                    size = 50,
                    angleJitter = 0.3f,
                    sizeJitter = 0.5f,
                    spacing = 0.07f,
                    scatter = 0.55f
                ), listOf(ir.simurgh.photolib.R.drawable.free_charcoal_bruses_2)
            ),

            ResourceBrush(
                BitmapBrush(
                    size = 50,
                    angle = 100f,
                    spacing = 0.06f,
                    autoRotate = true,
                    smoothness = 0.3f
                ), listOf(ir.simurgh.photolib.R.drawable.free_charcoal_bruses_7)
            ),

            ResourceBrush(
                BitmapBrush(
                    size = 50,
                    angleJitter = 0.5f,
                    sizeJitter = 0.3f,
                    spacing = 0.07f,
                    scatter = 0.04f
                ), listOf(ir.simurgh.photolib.R.drawable.free_charcoal_bruses_10)
            ),

            ResourceBrush(
                BitmapBrush(
                    size = 50,
                    angleJitter = 0.5f,
                    sizeJitter = 0.3f,
                    spacing = 0.06f,
                    scatter = 0.25f
                ), listOf(ir.simurgh.photolib.R.drawable.real_6)
            ),

            ResourceBrush(
                BitmapBrush(
                    size = 50,
                    angle = 4.5f,
                    spacing = 0.96f,
                    autoRotate = true
                ), listOf(ir.simurgh.photolib.R.drawable.wavy_hair)
            ),

            ResourceBrush(
                BitmapBrush(
                    size = 50,
                    angle = 125f,
                    spacing = 0.06f,
                    smoothness = 0.2f,
                    autoRotate = true
                ), listOf(R.drawable.char_brush_5)
            ),

            ResourceBrush(
                BitmapBrush(
                    size = 50,
                    angle = 4.5f,
                    spacing = 0.06f,
                    angleJitter = 1f,
                    sizeJitter = 0.4f
                ), listOf(R.drawable.char_brush_15)
            ),

            ResourceBrush(
                BitmapBrush(
                    size = 50,
                    spacing = 0.06f,
                    sizeJitter = 0.15f,
                    angleJitter = 1f
                ), listOf(R.drawable.char_brush_16)
            ),

            ResourceBrush(
                BitmapBrush(
                    size = 50,
                    spacing = 0.06f,
                    sizeJitter = 0.2f,
                    angleJitter = 1f
                ), listOf(R.drawable.char_brush_19)
            ),

            ResourceBrush(
                BitmapBrush(
                    size = 50,
                    spacing = 0.06f,
                    scatter = 0.1f,
                    sizeJitter = 0.3f,
                    autoRotate = true,
                    angleJitter = 0.08f
                ), listOf(R.drawable.char_brush_5)
            ),

            ResourceBrush(
                SpriteBrush(
                    size = 50,
                    spacing = 2f,
                    scatter = 2f,
                    sizeJitter = 1f,
                    angleJitter = 1f
                ).apply {
                    isColoringEnabled = true
                }, listOf(
                    R.drawable.star_brush,
                    R.drawable.star_brush_1,
                    R.drawable.star_brush_2,
                    R.drawable.star_brush_3,
                    R.drawable.star_brush_4
                )
            ),

            )
    }
}