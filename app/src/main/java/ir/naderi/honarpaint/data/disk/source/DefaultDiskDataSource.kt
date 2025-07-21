package ir.naderi.honarpaint.data.disk.source

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import ir.naderi.honarpaint.data.brush.model.Result
import ir.naderi.honarpaint.data.disk.model.ExportInfo
import javax.inject.Inject

class DefaultDiskDataSource @Inject constructor(@ApplicationContext private val context: Context) :
    DiskDataSource {
    override suspend fun saveBitmap(exportInfo: ExportInfo): Result<Unit> {
        exportInfo.run {
            context.contentResolver.openOutputStream(uri)?.use { stream ->
                try {
                    bitmap.compress(format, quality, stream)
                    stream.flush()
                    return Result.Success(Unit)
                } catch (e: Exception) {
                    return Result.Error(e.message ?: "Couldn't save file on device", e)
                } finally {
                    bitmap.recycle()
                }
            }
            return Result.Error("Couldn't save file on device")
        }
    }
}