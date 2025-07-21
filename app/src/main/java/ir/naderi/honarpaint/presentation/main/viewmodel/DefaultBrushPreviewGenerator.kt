package ir.naderi.honarpaint.presentation.main.viewmodel

import android.content.Context
import android.graphics.BitmapFactory
import android.graphics.Canvas
import androidx.core.graphics.createBitmap
import dagger.hilt.android.qualifiers.ApplicationContext
import ir.naderi.honarpaint.data.brush.model.ResourceBrush
import ir.naderi.honarpaint.presentation.main.model.BrushPreview
import ir.simurgh.photolib.components.paint.painters.painting.brushes.BitmapBrush
import ir.simurgh.photolib.components.paint.painters.painting.brushes.SpriteBrush
import ir.simurgh.photolib.components.paint.painters.painting.engines.DrawingEngine
import ir.simurgh.photolib.utils.gesture.TouchData
import javax.inject.Inject

class DefaultBrushPreviewGenerator @Inject constructor(
    @ApplicationContext private val context: Context,
    private val drawingEngine: DrawingEngine
) :
    BrushPreviewGenerator {

    private val canvas by lazy {
        Canvas()
    }


    override fun generatePreview(
        resourceBrush: ResourceBrush,
        renderSize: Int,
        bitmapSize: Int
    ): BrushPreview {
        return resourceBrush.brush.run {

            when (this) {
                is BitmapBrush -> {
                    changeBrushBitmap(
                        BitmapFactory.decodeResource(
                            context.resources,
                            resourceBrush.resourceId!!.first()
                        ), true
                    )
                }

                is SpriteBrush -> {
                    val list = resourceBrush.resourceId?.map { id ->
                        BitmapFactory.decodeResource(
                            context.resources,
                            id
                        )
                    }
                    changeBrushes(list, true)
                }
            }

            val currentBrushSize = size
            val currentScatter = scatter
            val currentSizeJitter = sizeJitter
            val currentAngleJitter = angleJitter

            size = renderSize
            scatter = 0f
            sizeJitter = 0f
            angleJitter = 0f

            val halfBitmapSize = bitmapSize * 0.5f
            val bitmap = createBitmap(bitmapSize, bitmapSize)

            canvas.setBitmap(bitmap)

            drawingEngine.onMoveBegin(TouchData(halfBitmapSize, halfBitmapSize), this)
            drawingEngine.draw(halfBitmapSize, halfBitmapSize, 0f, canvas, this, 1)

            size = currentBrushSize
            scatter = currentScatter
            sizeJitter = currentSizeJitter
            angleJitter = currentAngleJitter

            (this as? BitmapBrush)?.changeBrushBitmap(null, true)
            (this as? SpriteBrush)?.changeBrushes(null, true)

            BrushPreview(this, bitmap, resourceBrush.resourceId)
        }
    }
}
