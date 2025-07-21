package ir.naderi.honarpaint.presentation.shared

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
open class BaseViewModel @Inject constructor(): ViewModel() {
    protected val _toastMessageHolder = MutableLiveData<String?>()

    val toastMessage: LiveData<String?>
        get() = _toastMessageHolder

    protected val _isDoingOperation = MutableLiveData<Boolean?>(null)

    val isDoingOperation: LiveData<Boolean?>
        get() = _isDoingOperation


    protected open fun showCircularProgressAndShowToast(
        dispatcher: CoroutineDispatcher = Dispatchers.IO,
        block: suspend CoroutineScope.() -> Unit
    ) {
        viewModelScope.launch(dispatcher) {
            try {
                _isDoingOperation.postValue(true)
                block.invoke(this)
            } catch (e: Exception) {
                _toastMessageHolder.postValue(e.message)
            } finally {
                _isDoingOperation.postValue(false)
            }
        }
    }

    fun onDoingOperationShown() {
        _isDoingOperation.value = null
    }

    fun onToastShown() {
        _toastMessageHolder.value = null
    }
}