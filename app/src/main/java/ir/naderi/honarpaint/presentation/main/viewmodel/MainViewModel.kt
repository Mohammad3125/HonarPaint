package ir.naderi.honarpaint.presentation.main.viewmodel

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import androidx.core.graphics.createBitmap
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import ir.naderi.honarpaint.R
import ir.naderi.honarpaint.data.brush.model.Result
import ir.naderi.honarpaint.domain.brush.model.AppLimits
import ir.naderi.honarpaint.domain.brush.usecase.CalculateLimitsUseCase
import ir.naderi.honarpaint.domain.brush.usecase.GetBrushUseCase
import ir.naderi.honarpaint.domain.brush.usecase.ManageLayersUseCase
import ir.naderi.honarpaint.presentation.main.model.BrushPreview
import ir.naderi.honarpaint.presentation.main.model.Layer
import ir.naderi.honarpaint.presentation.shared.BaseViewModel
import ir.simurgh.photolib.components.paint.painters.coloring.flood.FloodFill
import ir.simurgh.photolib.components.paint.painters.painting.brushes.BitmapBrush
import ir.simurgh.photolib.components.paint.painters.painting.brushes.Brush
import ir.simurgh.photolib.components.paint.painters.painting.brushes.SpriteBrush
import ir.simurgh.photolib.components.paint.view.PaintLayer
import javax.inject.Inject


@HiltViewModel
class MainViewModel @Inject constructor(
    @ApplicationContext val context: Context,
    private val getBrushesUseCase: GetBrushUseCase,
    private val brushPreviewGenerator: BrushPreviewGenerator,
    private val calculateLimitsUseCase: CalculateLimitsUseCase,
    private val manageLayersUseCase: ManageLayersUseCase
) : BaseViewModel() {
    private val _brushPreview =
        MutableLiveData<List<BrushPreview>>()

    val brushPreviews: LiveData<List<BrushPreview>>
        get() = _brushPreview


    private val _firstPageBitmap = MutableLiveData<PaintLayer?>()

    val firstPageBitmap: LiveData<PaintLayer?>
        get() = _firstPageBitmap

    private val _appLimitsHolder = MutableLiveData<AppLimits>()

    val appLimitsHolder: LiveData<AppLimits>
        get() = _appLimitsHolder

    private val _addLayer = MutableLiveData<Boolean>()

    val addLayer: LiveData<Boolean>
        get() = _addLayer

    private val _isFloodFillDone = MutableLiveData<Boolean?>()

    val isFloodFillDone: LiveData<Boolean?>
        get() = _isFloodFillDone

    private val _currentBrush = MutableLiveData<Brush?>(null)

    val currentBrush: LiveData<Brush?>
        get() = _currentBrush

    private val _isBrushPreviewExpanded = MutableLiveData<Boolean>(false)

    val isBrushPreviewExpanded: LiveData<Boolean>
        get() = _isBrushPreviewExpanded

    private val _selectedLayer = MutableLiveData<PaintLayer?>(null)

    val selectedLayer: LiveData<PaintLayer?>
        get() = _selectedLayer

    private val _layers = MutableLiveData<List<Layer>?>()

    val layers: LiveData<List<Layer>?>
        get() = _layers

    private val _removedLayers = MutableLiveData<IntArray?>(null)

    val removedLayers: LiveData<IntArray?>
        get() = _removedLayers


    private val _mergedLayers = MutableLiveData<IntArray?>(null)

    val mergedLayer: LiveData<IntArray?>
        get() = _mergedLayers


    fun floodFill(bitmap: Bitmap, ex: Int, ey: Int, replaceColor: Int, algorithm: FloodFill) {
        showCircularProgressAndShowToast {
            _isFloodFillDone.postValue(false)
            algorithm.fill(bitmap, ex, ey, replaceColor, 0.1f)
            _isFloodFillDone.postValue(true)
        }
    }

    fun onAddLayerClicked(layersCount: Int) {
        _appLimitsHolder.value?.let { limits ->
            val isLimitReached = !manageLayersUseCase.canAddLayer(layersCount, limits)
            if (isLimitReached) {
                _toastMessageHolder.postValue(context.getString(R.string.layers_limit_reached))
            } else {
                _addLayer.postValue(true)
            }
        }
    }

    fun setFirstPageBitmap(bitmap: Bitmap) {
        showCircularProgressAndShowToast {
            calculateHistorySizeAndMaxLayerCount(bitmap.width, bitmap.height)

            loadBrushes(context.resources.getDimensionPixelSize(R.dimen.brush_preview_size))

            _firstPageBitmap.postValue(PaintLayer(bitmap))
        }
    }

    fun setFirstPageBitmap(width: Int?, height: Int?) {
        setFirstPageBitmap(createBitmap(width ?: 1000, height ?: 1000).apply {
            eraseColor(Color.WHITE)
        })
    }

    fun firstPageBitmapShown() {
        _firstPageBitmap.value = null
    }

    private suspend fun loadBrushes(renderSize: Int) {
        if (_brushPreview.value?.isNotEmpty() == true) {
            return
        }
        when (val result = getBrushesUseCase()) {
            is Result.Success -> {
                val listBrush = result.data.map {
                    brushPreviewGenerator.generatePreview(it, renderSize, renderSize)
                }

                _currentBrush.postValue(listBrush.firstOrNull()?.brush)
                _brushPreview.postValue(listBrush)
            }

            is Result.Error -> {
                _toastMessageHolder.postValue(result.message)
            }
        }
    }

    fun calculateHistorySizeAndMaxLayerCount(pageWidth: Int, pageHeight: Int) {
        val limits = calculateLimitsUseCase.invoke(pageWidth, pageHeight)
        _appLimitsHolder.postValue(limits)
    }

    fun onBrushClicked(brushPreview: BrushPreview) {
        val lastBrush = _currentBrush.value

        if (brushPreview.brush === lastBrush) {
            _isBrushPreviewExpanded.value = true
            initializeBrushBitmaps(brushPreview)
            return
        }

        when (lastBrush) {
            is BitmapBrush -> {
                lastBrush.changeBrushBitmap(null, true)
            }

            is SpriteBrush -> {
                lastBrush.changeBrushes(null, true)
            }
        }

        val newBrush = brushPreview.brush

        _currentBrush.value = newBrush
        newBrush.color = lastBrush?.color ?: Color.BLACK

        initializeBrushBitmaps(brushPreview)
    }

    private fun initializeBrushBitmaps(brushPreview: BrushPreview) {
        val brush = brushPreview.brush
        when (brush) {
            is BitmapBrush -> {
                brush.changeBrushBitmap(
                    BitmapFactory.decodeResource(
                        context.resources,
                        brushPreview.drawableId!!.first()
                    ), true
                )
            }

            is SpriteBrush -> {
                val list = brushPreview.drawableId?.map { id ->
                    BitmapFactory.decodeResource(
                        context.resources,
                        id
                    )
                }
                brush.changeBrushes(list, true)
            }
        }
    }

    fun onLayersChanged(layers: List<PaintLayer>, selectedLayerIndex: Int) {
        if (layers.isEmpty()) {
            _layers.value = null
            return
        }

        if (selectedLayerIndex < 0) {
            return
        }

        _selectedLayer.value = layers[selectedLayerIndex]

        _layers.value = layers.mapIndexed { index, paintLayer ->
            Layer(
                index,
                paintLayer,
                index == selectedLayerIndex
            )
        }
    }

    fun onExpandShown() {
        _isBrushPreviewExpanded.value = false
    }

    fun onLayerDeleted(list: List<Layer>, isCheckMode: Boolean) {
        when (val result = manageLayersUseCase.deleteLayers(list, isCheckMode)) {
            is Result.Error -> {
                _toastMessageHolder.value = context.getString(R.string.can_t_delete_first_layer)
            }

            is Result.Success -> {
                _removedLayers.value = result.data.toIntArray()
            }
        }
    }

    fun onMergeLayouts(list: List<Layer>) {
        _mergedLayers.value = manageLayersUseCase.mergeLayers(list).toIntArray()
    }

    fun onRemovedLayerCalled() {
        _removedLayers.value = null
    }


    fun onMergeLayersCalled() {
        _mergedLayers.value = null
    }

    fun onLayerAdded() {
        _addLayer.value = false
    }

    fun onFloodFilled() {
        _isFloodFillDone.value = null
    }

}