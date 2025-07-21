package ir.naderi.honarpaint.domain.brush.device

interface DeviceInfoProvider {
    fun getAvailableMemory(): Int
    fun getLargeMemoryClass(): Int
}
