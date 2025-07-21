package ir.naderi.honarpaint.data.brush.repository

import ir.naderi.honarpaint.data.brush.model.ResourceBrush
import ir.naderi.honarpaint.data.brush.model.Result
import ir.naderi.honarpaint.data.brush.source.ResourceDataSource
import ir.naderi.honarpaint.di.MainDispatcher
import ir.naderi.honarpaint.domain.brush.repository.BrushRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class DefaultBrushRepository @Inject constructor(
    @MainDispatcher val dispatcher: CoroutineDispatcher,
    val resourceDataSource: ResourceDataSource
) : BrushRepository {

    override suspend fun getBrush(): Result<List<ResourceBrush>> {
        return withContext(dispatcher) {
            try {
                val brushes = resourceDataSource.getBrushes()

                if (brushes.isEmpty()) {
                    Result.Error("No brushes found.")
                } else {
                    Result.Success(brushes)
                }
            } catch (e: Exception) {
                Result.Error("Failed to fetch brushes: ${e.localizedMessage}", e)
            }
        }
    }

}