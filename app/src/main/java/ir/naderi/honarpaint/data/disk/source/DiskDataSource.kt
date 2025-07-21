package ir.naderi.honarpaint.data.disk.source

import ir.naderi.honarpaint.data.brush.model.Result
import ir.naderi.honarpaint.data.disk.model.ExportInfo

interface DiskDataSource {
    suspend fun saveBitmap(exportInfo: ExportInfo): Result<Unit>
}