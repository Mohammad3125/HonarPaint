package ir.naderi.honarpaint.data.brush.source

import ir.naderi.honarpaint.data.brush.model.ResourceBrush

interface ResourceDataSource {
    suspend fun getBrushes(): List<ResourceBrush>
}