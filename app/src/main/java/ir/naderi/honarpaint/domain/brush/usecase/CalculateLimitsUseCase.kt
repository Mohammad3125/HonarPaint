package ir.naderi.honarpaint.domain.brush.usecase

import ir.naderi.honarpaint.domain.brush.device.DeviceInfoProvider
import ir.naderi.honarpaint.domain.brush.model.AppLimits
import kotlin.math.floor
import kotlin.math.max
import kotlin.math.roundToInt
import javax.inject.Inject

class CalculateLimitsUseCase @Inject constructor(
    private val deviceInfoProvider: DeviceInfoProvider
) {
    fun invoke(pageWidth: Int, pageHeight: Int): AppLimits {
        val availableMemory = max(
            deviceInfoProvider.getAvailableMemory(),
            deviceInfoProvider.getLargeMemoryClass()
        )
        val eachBitmapSizeMegabyte = ((pageWidth * pageHeight) * 8) * 0.000001
        val totalBitmapsForAvailableMemory =
            floor((availableMemory / eachBitmapSizeMegabyte)).toInt()
        var historySize = (totalBitmapsForAvailableMemory * 0.6).roundToInt()
        var layersCount = totalBitmapsForAvailableMemory - historySize
        if (layersCount == 1) {
            layersCount = 2
            historySize -= 1
        }
        val isCachingEnabled = layersCount > 4
        return AppLimits(
            historySize,
            layersCount,
            isCachingEnabled,
            layersCount < 4
        )
    }
}