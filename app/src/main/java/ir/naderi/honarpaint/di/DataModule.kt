package ir.naderi.honarpaint.di

import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ir.naderi.honarpaint.data.brush.repository.DefaultBrushRepository
import ir.naderi.honarpaint.data.brush.source.DefaultResourceDataSource
import ir.naderi.honarpaint.data.brush.source.ResourceDataSource
import ir.naderi.honarpaint.data.disk.repositories.DefaultDiskRepository
import ir.naderi.honarpaint.data.disk.source.DefaultDiskDataSource
import ir.naderi.honarpaint.data.disk.source.DiskDataSource
import ir.naderi.honarpaint.data.system.DeviceInfoProviderImpl
import ir.naderi.honarpaint.domain.brush.repository.BrushRepository
import ir.naderi.honarpaint.domain.brush.device.DeviceInfoProvider
import ir.naderi.honarpaint.domain.disk.repository.DiskRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DataModuleBinder {
    @Binds
    @Singleton
    abstract fun bindResourceDataSource(defaultResourceDataSource: DefaultResourceDataSource): ResourceDataSource

    @Binds
    @Singleton
    abstract fun bindBrushRepository(defaultBrushRepository: DefaultBrushRepository): BrushRepository

    @Binds
    @Singleton
    abstract fun bindDiskRepository(defaultDiskRepository: DefaultDiskRepository): DiskRepository

    @Binds
    @Singleton
    abstract fun bindDiskDataSource(defaultDiskDataSource: DefaultDiskDataSource): DiskDataSource

    @Binds
    @Singleton
    abstract fun bindDeviceInfoProvider(
        impl: DeviceInfoProviderImpl
    ): DeviceInfoProvider
}


@Module
@InstallIn(SingletonComponent::class)
object DataModuleProvider {
    @Provides
    @Singleton
    fun provideResourceDataSource(): DefaultResourceDataSource = DefaultResourceDataSource()
}