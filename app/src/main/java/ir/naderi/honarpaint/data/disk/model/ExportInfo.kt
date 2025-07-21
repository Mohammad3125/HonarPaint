package ir.naderi.honarpaint.data.disk.model

import android.graphics.Bitmap
import android.net.Uri

data class ExportInfo(
    val bitmap: Bitmap,
    val uri: Uri,
    val quality: Int = 100,
    val format: Bitmap.CompressFormat = Bitmap.CompressFormat.PNG
)
