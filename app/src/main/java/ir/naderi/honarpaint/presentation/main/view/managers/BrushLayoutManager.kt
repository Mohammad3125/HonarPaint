package ir.naderi.honarpaint.presentation.main.view.managers

import ir.naderi.honarpaint.databinding.BrushesLayoutBinding
import ir.naderi.honarpaint.presentation.main.model.BrushPreview
import ir.naderi.honarpaint.presentation.main.view.adapters.RecyclerViewBrushPreviewAdapter

class BrushLayoutManager(private val binding: BrushesLayoutBinding) {

    private lateinit var brushAdapter: RecyclerViewBrushPreviewAdapter

    var onBrushItemClicked: ((brushPreview: BrushPreview) -> Unit)? = null

    init {
        initialize()
    }

    private fun initialize() {
        binding.apply {
            brushAdapter = RecyclerViewBrushPreviewAdapter().apply {
                onItemClicked { brushPreview, _ ->
                    onBrushItemClicked?.invoke(brushPreview)
                }
            }

            recyclerViewBrushesPreview.adapter = brushAdapter
        }
    }

    fun submitBrushes(brushes: List<BrushPreview>) {
        brushAdapter.submitList(brushes)
    }
}