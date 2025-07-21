package ir.naderi.honarpaint.domain.disk.repository

import ir.naderi.honarpaint.data.brush.model.Result
import ir.naderi.honarpaint.data.disk.model.ExportInfo

interface DiskRepository {
    suspend fun saveBitmap(exportInfo: ExportInfo): Result<Unit>
}