package ir.naderi.honarpaint.data.system

import ir.naderi.honarpaint.domain.brush.device.DeviceInfoProvider
import android.app.ActivityManager
import javax.inject.Inject

class DeviceInfoProviderImpl @Inject constructor(
    private val activityManager: ActivityManager
) : DeviceInfoProvider {
    override fun getAvailableMemory(): Int {
        val memoryInfo = ActivityManager.MemoryInfo()
        activityManager.getMemoryInfo(memoryInfo)
        return ((memoryInfo.availMem - memoryInfo.threshold) * 0.000001).toInt()
    }

    override fun getLargeMemoryClass(): Int = activityManager.largeMemoryClass
}
