package ir.naderi.honarpaint.presentation.export.viewmodel

import android.graphics.Bitmap
import android.os.Build
import androidx.activity.result.ActivityResult
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import ir.naderi.honarpaint.data.brush.model.Result
import ir.naderi.honarpaint.data.disk.model.ExportInfo
import ir.naderi.honarpaint.domain.disk.repository.DiskRepository
import ir.naderi.honarpaint.domain.disk.usecase.SelectExportTypeUseCase
import ir.naderi.honarpaint.presentation.export.model.ExportData
import ir.naderi.honarpaint.presentation.shared.BaseViewModel
import javax.inject.Inject

@HiltViewModel
class ExportViewModel @Inject constructor(
    private val diskRepository: DiskRepository,
    private val selectExportTypeUseCase: SelectExportTypeUseCase
) :
    BaseViewModel() {

    private var exportType: Bitmap.CompressFormat = Bitmap.CompressFormat.PNG
    private var exportQuality: Int = 100

    private val _isQualitySliderEnabled = MutableLiveData(true)
    val isQualitySliderEnabled: LiveData<Boolean> = _isQualitySliderEnabled

    private val _onSavedSuccessfully = MutableLiveData<Boolean>(false)

    val onSavedSuccessfully: LiveData<Boolean>
        get() = _onSavedSuccessfully

    private val _exportData = MutableLiveData<ExportData>()

    val exportData: LiveData<ExportData>
        get() = _exportData

    fun onExportTypeSelected(chipText: CharSequence) {
        val result = selectExportTypeUseCase(chipText)
        exportType = result.exportType
        _isQualitySliderEnabled.value = result.isQualitySliderEnabled
    }

    fun onExportQualityChanged(quality: Int) {
        exportQuality = quality
    }

    fun setExportBitmap(bitmap: Bitmap) {
        _exportData.value = ExportData(bitmap, "${bitmap.width} * ${bitmap.height}")
    }

    fun getDefaultFileName(baseName: String): String {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R &&
            (exportType == Bitmap.CompressFormat.WEBP_LOSSLESS || exportType == Bitmap.CompressFormat.WEBP_LOSSY)
        ) {
            "$baseName.WEBP"
        } else {
            "$baseName.${exportType.name}"
        }
    }

    fun onSave(
        result: ActivityResult
    ) {
        showCircularProgressAndShowToast {
            if (result.resultCode == AppCompatActivity.RESULT_OK) {
                result.data?.data?.let { uri ->
                    val exportInfo = ExportInfo(
                        _exportData.value?.bitmap!!,
                        uri,
                        exportQuality,
                        exportType
                    )
                    when (val result = diskRepository.saveBitmap(exportInfo)) {
                        is Result.Success<*> -> {
                            _toastMessageHolder.postValue("File saved successfully")
                            _onSavedSuccessfully.postValue(true)
                        }

                        is Result.Error -> _toastMessageHolder.postValue(result.message)
                    }
                }
            }
        }
    }
}