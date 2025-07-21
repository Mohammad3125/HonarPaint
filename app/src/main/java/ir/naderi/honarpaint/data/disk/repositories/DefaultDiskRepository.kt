package ir.naderi.honarpaint.data.disk.repositories


import ir.naderi.honarpaint.data.brush.model.Result
import ir.naderi.honarpaint.data.disk.model.ExportInfo
import ir.naderi.honarpaint.data.disk.source.DiskDataSource
import ir.naderi.honarpaint.di.IoDispatcher
import ir.naderi.honarpaint.domain.disk.repository.DiskRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class DefaultDiskRepository @Inject constructor(
    @IoDispatcher val dispatcher: CoroutineDispatcher,
    private val diskDataSource: DiskDataSource
) : DiskRepository {
    override suspend fun saveBitmap(exportInfo: ExportInfo): Result<Unit> {
        return withContext(dispatcher) {
            diskDataSource.saveBitmap(exportInfo)
        }
    }
}