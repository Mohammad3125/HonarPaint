package ir.naderi.honarpaint.domain.brush.model

data class AppLimits(
    val historySize: Int,
    val maxLayerCount: Int,
    val isCachingEnabled: Boolean,
    val isLowMemoryState: Boolean,
)