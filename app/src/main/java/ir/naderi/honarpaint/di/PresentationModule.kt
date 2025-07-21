package ir.naderi.honarpaint.di

import android.app.ActivityManager
import android.content.Context
import androidx.core.content.getSystemService
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import ir.naderi.honarpaint.presentation.main.viewmodel.BrushPreviewGenerator
import ir.naderi.honarpaint.presentation.main.viewmodel.DefaultBrushPreviewGenerator
import ir.simurgh.photolib.components.paint.painters.painting.engines.CanvasDrawingEngine
import ir.simurgh.photolib.components.paint.painters.painting.engines.DrawingEngine
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object PresentationBinder {

    @Provides
    @Singleton
    fun provideActivityManager(@ApplicationContext context: Context): ActivityManager =
        context.getSystemService<ActivityManager>()!!


    @Provides
    @Singleton
    fun provideDrawingEngine(): DrawingEngine {
        return CanvasDrawingEngine()
    }

}

@Module
@InstallIn(SingletonComponent::class)
abstract class PresentationProvider {
    @Binds
    @Singleton
    abstract fun bindBrushPreviewGenerator(defaultBrushPreviewGenerator: DefaultBrushPreviewGenerator): BrushPreviewGenerator
}

