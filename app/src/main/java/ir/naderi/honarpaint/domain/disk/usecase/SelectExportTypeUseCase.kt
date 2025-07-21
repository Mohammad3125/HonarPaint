package ir.naderi.honarpaint.domain.disk.usecase

import android.graphics.Bitmap
import android.os.Build
import ir.naderi.honarpaint.domain.disk.model.ExportTypeSelectionResult
import javax.inject.Inject


class SelectExportTypeUseCase @Inject constructor() {
    operator fun invoke(chipText: CharSequence): ExportTypeSelectionResult {
        return when (chipText) {
            "PNG" -> ExportTypeSelectionResult(Bitmap.CompressFormat.PNG, true)
            "JPG" -> ExportTypeSelectionResult(Bitmap.CompressFormat.JPEG, true)
            "WEBP" -> ExportTypeSelectionResult(Bitmap.CompressFormat.WEBP, true)

            "WEBP LOSSLESS" -> if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                ExportTypeSelectionResult(Bitmap.CompressFormat.WEBP_LOSSLESS, false)
            } else {
                ExportTypeSelectionResult(Bitmap.CompressFormat.WEBP, true)
            }

            "WEBP LOSSY" -> if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                ExportTypeSelectionResult(Bitmap.CompressFormat.WEBP_LOSSY, true)
            } else {
                ExportTypeSelectionResult(Bitmap.CompressFormat.WEBP, true)
            }

            else -> ExportTypeSelectionResult(Bitmap.CompressFormat.WEBP, true)
        }
    }
}
