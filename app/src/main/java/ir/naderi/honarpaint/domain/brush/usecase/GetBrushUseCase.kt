package ir.naderi.honarpaint.domain.brush.usecase

import ir.naderi.honarpaint.data.brush.model.ResourceBrush
import ir.naderi.honarpaint.data.brush.model.Result
import ir.naderi.honarpaint.domain.brush.repository.BrushRepository
import javax.inject.Inject

class GetBrushUseCase @Inject constructor(private val brushRepository: BrushRepository) {
    suspend operator fun invoke(): Result<List<ResourceBrush>> =
        brushRepository.getBrush()
}
