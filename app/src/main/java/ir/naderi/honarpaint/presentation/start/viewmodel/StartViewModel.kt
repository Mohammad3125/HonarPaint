package ir.naderi.honarpaint.presentation.start.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import ir.naderi.honarpaint.domain.paint.model.CanvasDimensions
import ir.naderi.honarpaint.domain.paint.usecase.ValidateCanvasDimensionsUseCase
import ir.naderi.honarpaint.presentation.shared.BaseViewModel
import javax.inject.Inject

@HiltViewModel
class StartViewModel @Inject constructor(
    private val validateCanvasDimensionsUseCase: ValidateCanvasDimensionsUseCase
) : BaseViewModel() {

    private val _navigateToPaint = MutableLiveData<CanvasDimensions?>()
    val navigateToPaint: LiveData<CanvasDimensions?> = _navigateToPaint
    fun onStartClicked(widthInput: String, heightInput: String) {
        val width = widthInput.toIntOrNull() ?: -1
        val height = heightInput.toIntOrNull() ?: -1
        val dimensions = CanvasDimensions(width, height)
        if (validateCanvasDimensionsUseCase(dimensions)) {
            _navigateToPaint.value = dimensions
        } else {
            _toastMessageHolder.value = "Width and height must be positive integers."
        }
    }

    fun onNavigationHandled() {
        _navigateToPaint.value = null
    }
}