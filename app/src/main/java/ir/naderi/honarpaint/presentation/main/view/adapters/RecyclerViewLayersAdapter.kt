package ir.naderi.honarpaint.presentation.main.view.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.google.android.material.checkbox.MaterialCheckBox
import ir.simurgh.photolib.components.paint.view.PaintLayer
import ir.naderi.honarpaint.R
import ir.naderi.honarpaint.presentation.main.model.Layer
import kotlin.also
import kotlin.apply
import kotlin.collections.map
import kotlin.let

class RecyclerViewLayersAdapter(
    val onClick: (Layer) -> Unit,
) :
    ListAdapter<Layer, RecyclerViewLayersAdapter.PaintLayerViewHolder>(
        PaintLayerDiff
    ) {

    var isCheckModeEnabled = true
    var isCheckMode = false
        set(value) {
            field = value
            onItemCheckingModeChanged?.invoke(currentList, value)
            notifyDataSetChanged()
        }

    private var onItemCheckingModeChanged: ((list: List<Layer>, isInCheckingMode: Boolean) -> Unit)? =
        null

    inner class PaintLayerViewHolder(val view: View, val onClick: (Layer) -> Unit) :
        ViewHolder(view) {
        private var paintLayer: Layer? = null
        var checkBox: MaterialCheckBox? = null

        init {
            view.apply {
                setOnClickListener {
                    if (isCheckMode && isCheckModeEnabled) {
                        checkPosition()
                    } else {
                        paintLayer?.let(onClick)
                    }
                    notifyItemChanged(adapterPosition)
                }
                setOnLongClickListener {
                    if (isCheckModeEnabled) {
                        submitList(currentList.map { it.also { it.isChecked = false } })
                        checkPosition()
                        onItemCheckingModeChanged?.invoke(currentList, true)
                        isCheckMode = true
                    }
                    false
                }
            }
        }

        fun bind(paintLayer: Layer) {
            this.paintLayer = paintLayer

            view.findViewById<AppCompatImageView>(R.id.imageViewLayer)
                .setImageBitmap(paintLayer.paintLayer.bitmap)

            view.findViewById<AppCompatImageView>(R.id.imageViewLayerLockIcon).visibility =
                if (paintLayer.paintLayer.isLocked) View.VISIBLE else View.GONE

            view.findViewById<AppCompatImageView>(R.id.imageViewLayerVisibilityIcon).visibility =
                if (paintLayer.paintLayer.opacity > 0f) View.VISIBLE else View.INVISIBLE

            view.findViewById<View>(R.id.viewIsLayerSelected).visibility =
                if (paintLayer.isSelected) View.VISIBLE else View.GONE

            checkBox = view.findViewById<MaterialCheckBox>(R.id.checkboxLayers).apply {
                visibility = if (isCheckMode) {
                    View.VISIBLE
                } else {
                    View.GONE
                }
                setOnClickListener {
                    checkPosition()
                }

                isChecked = getItem(adapterPosition).isChecked
            }

        }

        private fun checkPosition() {
            val item = getItem(adapterPosition)
            item.isChecked = !item.isChecked
            checkBox?.isChecked = item.isChecked
            onItemCheckingModeChanged?.invoke(currentList, true)
        }
    }

    fun onItemCheckingModeChanged(func: (currentList: List<Layer>, isInCheckingMode: Boolean) -> Unit) {
        onItemCheckingModeChanged = func
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PaintLayerViewHolder {
        return PaintLayerViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.layers_item_layout, parent, false),
            onClick
        )

    }


    override fun onBindViewHolder(holder: PaintLayerViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    object PaintLayerDiff : DiffUtil.ItemCallback<Layer>() {
        override fun areItemsTheSame(
            oldItem: Layer,
            newItem: Layer
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: Layer,
            newItem: Layer
        ): Boolean {
            return oldItem.paintLayer == newItem.paintLayer && oldItem.isSelected == newItem.isSelected
        }
    }


}