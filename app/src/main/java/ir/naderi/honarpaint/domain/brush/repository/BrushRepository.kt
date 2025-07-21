package ir.naderi.honarpaint.domain.brush.repository

import ir.naderi.honarpaint.data.brush.model.ResourceBrush
import ir.naderi.honarpaint.data.brush.model.Result

interface BrushRepository {
    suspend fun getBrush(): Result<List<ResourceBrush>>
}