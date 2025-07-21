package ir.naderi.honarpaint.domain.disk.model

import android.graphics.Bitmap

data class ExportTypeSelectionResult(
    val exportType: Bitmap.CompressFormat,
    val isQualitySliderEnabled: Boolean
)