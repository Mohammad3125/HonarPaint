package ir.naderi.honarpaint.presentation.shared

import android.graphics.Bitmap
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SharedViewModel @Inject constructor() : ViewModel() {
    private val _selectedBitmap = MutableLiveData<Bitmap?>()


    val selectedBitmap: LiveData<Bitmap?>
        get() {
            return _selectedBitmap
        }

    fun selectBitmap(bitmap: Bitmap) {
        _selectedBitmap.value = bitmap
    }

    fun onSelectedBitmapShown() {
        _selectedBitmap.value = null
    }
}