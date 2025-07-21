package ir.naderi.honarpaint.domain.brush.usecase

import ir.naderi.honarpaint.presentation.main.model.Layer
import ir.naderi.honarpaint.data.brush.model.Result
import ir.naderi.honarpaint.domain.brush.model.AppLimits
import javax.inject.Inject

class ManageLayersUseCase @Inject constructor() {
    fun canAddLayer(currentLayersCount: Int, limits: AppLimits): Boolean {
        return (currentLayersCount + 1) <= limits.maxLayerCount
    }

    fun deleteLayers(layers: List<Layer>, isCheckMode: Boolean): Result<List<Int>> {
        return if (isCheckMode) {
            val indices = layers.mapIndexedNotNull { index, paintLayerRecycler ->
                paintLayerRecycler.takeIf { it.isChecked }?.let { index }
            }
            if (indices.contains(0)) {
                Result.Error("Can't delete the first layer.")
            } else {
                Result.Success(indices)
            }
        } else {
            val selectedIndex = layers.indexOfFirst { it.isSelected }
            return if (selectedIndex == 0) {
                Result.Error("Can't delete the first layer.")
            } else {
                Result.Success(listOf(selectedIndex))
            }
        }
    }

    fun mergeLayers(layers: List<Layer>): List<Int> {
        return layers.mapIndexedNotNull { index, paintLayerRecycler ->
            paintLayerRecycler.takeIf { it.isChecked }?.let { index }
        }
    }
}
