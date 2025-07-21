package ir.naderi.honarpaint.presentation.main.view.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.graphics.drawable.toDrawable
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import ir.naderi.honarpaint.R
import ir.naderi.honarpaint.databinding.ItemBrushPreviewBinding
import ir.naderi.honarpaint.presentation.main.model.BrushPreview
import ir.simurgh.photolib.utils.extensions.dpInt
import kotlin.apply

class RecyclerViewBrushPreviewAdapter :
    ListAdapter<BrushPreview, RecyclerViewBrushPreviewAdapter.BrushPreviewViewHolder>(
        BrushDiff
    ) {

    private var onClick: ((BrushPreview, position: Int) -> Unit)? = null

    private var currentSelectedItem = -1
    private var lastSelectedItem = -1

    inner class BrushPreviewViewHolder(val view: View) :
        ViewHolder(view) {

        init {
            view.setOnClickListener {
                currentSelectedItem = adapterPosition
                if (lastSelectedItem != -1) {
                    notifyItemChanged(lastSelectedItem)
                }
                lastSelectedItem = currentSelectedItem
                notifyItemChanged(currentSelectedItem)
                onClick?.invoke(getItem(adapterPosition), adapterPosition)
            }
        }

        fun bind(preview: BrushPreview, isSelected: Boolean) {
            ItemBrushPreviewBinding.bind(view).apply {
                val strokeSize = view.dpInt(2)
                if (isSelected && cardViewBrushPreview.strokeWidth != strokeSize) {
                    cardViewBrushPreview.strokeWidth = strokeSize
                    viewSurfaceColorBehindSlidersHorizontal.visibility = View.VISIBLE
                    imageViewBrushPreviewSettingIcon.visibility = View.VISIBLE
                } else if (!isSelected && cardViewBrushPreview.strokeWidth != 0) {
                    cardViewBrushPreview.strokeWidth = 0
                    viewSurfaceColorBehindSlidersHorizontal.visibility = View.GONE
                    imageViewBrushPreviewSettingIcon.visibility = View.GONE
                }

                imageViewBrushPreview.background =
                    preview.brushBitmap.toDrawable(view.resources)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BrushPreviewViewHolder {
        return BrushPreviewViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_brush_preview, parent, false)
        )
    }

    override fun onBindViewHolder(holder: BrushPreviewViewHolder, position: Int) {
        holder.bind(getItem(position), position == currentSelectedItem)
    }

    object BrushDiff : DiffUtil.ItemCallback<BrushPreview>() {
        override fun areItemsTheSame(
            oldItem: BrushPreview,
            newItem: BrushPreview
        ): Boolean {
            return oldItem.brush == newItem.brush
        }

        override fun areContentsTheSame(
            oldItem: BrushPreview,
            newItem: BrushPreview
        ): Boolean {
            return oldItem.brushBitmap.sameAs(newItem.brushBitmap)
        }
    }

    fun onItemClicked(onItemClicked: (BrushPreview, position: Int) -> Unit) {
        onClick = onItemClicked
    }


}