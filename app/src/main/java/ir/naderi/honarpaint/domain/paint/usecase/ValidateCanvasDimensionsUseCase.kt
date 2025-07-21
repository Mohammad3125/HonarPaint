package ir.naderi.honarpaint.domain.paint.usecase

import ir.naderi.honarpaint.domain.paint.model.CanvasDimensions
import javax.inject.Inject

class ValidateCanvasDimensionsUseCase @Inject constructor() {
    operator fun invoke(dimensions: CanvasDimensions): Boolean {
        return dimensions.width > 0 && dimensions.height > 0
    }
}
