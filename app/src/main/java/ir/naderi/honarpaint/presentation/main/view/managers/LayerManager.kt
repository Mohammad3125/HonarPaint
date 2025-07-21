package ir.naderi.honarpaint.presentation.main.view.managers

import android.graphics.drawable.Drawable
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.SimpleItemAnimator
import ir.naderi.honarpaint.R
import ir.naderi.honarpaint.databinding.PainterViewLayersLayoutBinding
import ir.naderi.honarpaint.presentation.main.model.Layer
import ir.naderi.honarpaint.presentation.main.view.adapters.RecyclerViewLayersAdapter
import ir.naderi.honarpaint.utils.extensions.animateAlphaWithAppStyle
import ir.naderi.honarpaint.utils.extensions.gone
import ir.naderi.honarpaint.utils.extensions.visible
import ir.simurgh.photolib.components.paint.view.PaintLayer

class LayerManager(private val binding: PainterViewLayersLayoutBinding) {

    private lateinit var layersRecyclerViewAdapter: RecyclerViewLayersAdapter
    var onLayerSelected: ((PaintLayer) -> Unit)? = null

    var onLayerTransformed: (() -> Unit)? = null
    var onLayerAdded: (() -> Unit)? = null
    var onLayerDeleted: ((List<Layer>) -> Unit)? = null

    var onLayerDeletedLong: ((List<Layer>) -> Unit)? = null
    var onLayerCopied: (() -> Unit)? = null
    var onLayerMerged: ((List<Layer>) -> Unit)? = null
    var onLayerHidden: (() -> Unit)? = null
    var onLayerLocked: (() -> Unit)? = null
    var onLayerUp: (() -> Unit)? = null
    var onLayerDown: (() -> Unit)? = null
    var onLayerSetting: (() -> Unit)? = null

    private lateinit var lockDrawable: Drawable
    private lateinit var unlockDrawable: Drawable
    private lateinit var eyeDrawable: Drawable
    private lateinit var eyeCloseDrawable: Drawable

    var isCheckMode = false
        set(value) {
            field = value
            layersRecyclerViewAdapter.isCheckMode = field
        }
        get() = layersRecyclerViewAdapter.isCheckMode

    init {
        initializeDrawables()
        initialize()
    }


    private fun initializeDrawables() {
        binding.root.context.apply {
            lockDrawable = ResourcesCompat.getDrawable(
                resources,
                R.drawable.ic_lock,
                theme
            )!!

            unlockDrawable = ResourcesCompat.getDrawable(
                resources,
                R.drawable.ic_unlock,
                theme
            )!!

            eyeDrawable = ResourcesCompat.getDrawable(
                resources,
                R.drawable.ic_eye,
                theme
            )!!

            eyeCloseDrawable = ResourcesCompat.getDrawable(
                resources,
                R.drawable.ic_eye_close,
                theme
            )!!
        }
    }

    private fun initialize() {
        binding.apply {
            (recycleViewLayers.itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false

            layersRecyclerViewAdapter = RecyclerViewLayersAdapter {
                onLayerSelected?.invoke(it.paintLayer)
            }

            layersRecyclerViewAdapter.onItemCheckingModeChanged { currentList, isInCheckingMode ->
                val checkedList = currentList.filter { it.isChecked }
                val mergeEnabledState = checkedList.size > 1
                if (isInCheckingMode && checkedList.isNotEmpty()) {

                    buttonCopyLayers.gone()
                    buttonMergeLayers.visible()

                    buttonAddLayer.animateAlphaWithAppStyle(0.5f)
                    buttonAddLayer.isEnabled = false
                    buttonLayerUp.animateAlphaWithAppStyle(0.5f)
                    buttonLayerUp.isEnabled = false
                    buttonLayerDown.animateAlphaWithAppStyle(0.5f)
                    buttonLayerDown.isEnabled = false
                    buttonLayerTransform.animateAlphaWithAppStyle(0.5f)
                    buttonLayerTransform.isEnabled = false
                    buttonLayerSetting.animateAlphaWithAppStyle(0.5f)
                    buttonLayerSetting.isEnabled = false
                    buttonHideLayer.animateAlphaWithAppStyle(0.5f)
                    buttonHideLayer.isEnabled = false
                    buttonDeleteLayer.animateAlphaWithAppStyle(0.5f)
                    buttonDeleteLayer.isEnabled = checkedList.isNotEmpty()
                    buttonLockLayer.animateAlphaWithAppStyle(0.5f)
                    buttonLockLayer.isEnabled = false

                    buttonMergeLayers.isEnabled = mergeEnabledState

                    if (mergeEnabledState) {
                        buttonMergeLayers.animateAlphaWithAppStyle(1f)
                    } else {
                        buttonMergeLayers.animateAlphaWithAppStyle(0.5f)
                    }

                    if (buttonDeleteLayer.isEnabled) {
                        buttonDeleteLayer.animateAlphaWithAppStyle(1f)
                    }

                } else {
                    buttonCopyLayers.visible()
                    buttonMergeLayers.gone()
                    buttonAddLayer.animateAlphaWithAppStyle(1f)
                    buttonAddLayer.isEnabled = true
                    buttonLayerUp.animateAlphaWithAppStyle(1f)
                    buttonLayerUp.isEnabled = true
                    buttonLayerDown.animateAlphaWithAppStyle(1f)
                    buttonLayerDown.isEnabled = true
                    buttonLayerTransform.animateAlphaWithAppStyle(1f)
                    buttonLayerTransform.isEnabled = true
                    buttonLayerSetting.animateAlphaWithAppStyle(1f)
                    buttonLayerSetting.isEnabled = true
                    buttonMergeLayers.isEnabled = false
                    buttonMergeLayers.animateAlphaWithAppStyle(0.5f)
                    buttonHideLayer.animateAlphaWithAppStyle(1f)
                    buttonHideLayer.isEnabled = true
                    buttonDeleteLayer.animateAlphaWithAppStyle(1f)
                    buttonDeleteLayer.isEnabled = true
                    buttonLockLayer.animateAlphaWithAppStyle(1f)
                    buttonLockLayer.isEnabled = true

                    if (layersRecyclerViewAdapter.isCheckMode) {
                        layersRecyclerViewAdapter.isCheckMode = false
                    }
                }
            }

            recycleViewLayers.adapter = layersRecyclerViewAdapter

            buttonLayerTransform.setOnClickListener {
                onLayerTransformed?.invoke()
            }

            buttonMergeLayers.setOnClickListener {
                onLayerMerged?.invoke(layersRecyclerViewAdapter.currentList)
            }

            buttonCopyLayers.setOnClickListener {
                onLayerCopied?.invoke()
            }

            buttonLayerSetting.setOnClickListener {
                onLayerSetting?.invoke()
            }

            buttonAddLayer.setOnClickListener {
                onLayerAdded?.invoke()
            }

            buttonDeleteLayer.apply {
                setOnClickListener {
                    onLayerDeleted?.invoke(layersRecyclerViewAdapter.currentList)
                }
                setOnLongClickListener {
                    onLayerDeletedLong?.invoke(layersRecyclerViewAdapter.currentList)
                    true
                }
            }

            buttonLockLayer.setOnClickListener {
                onLayerLocked?.invoke()
            }

            buttonHideLayer.setOnClickListener {
                onLayerHidden?.invoke()
            }

            buttonLayerUp.setOnClickListener {
                onLayerUp?.invoke()
            }

            buttonLayerDown.setOnClickListener {
                onLayerDown?.invoke()
            }
        }
    }

    fun changeLockedAndOpacityDrawablesWithLayer(layer: PaintLayer) {
        binding.apply {
            buttonLockLayer.setImageDrawable(
                if (layer.isLocked) {
                    lockDrawable
                } else {
                    unlockDrawable
                }
            )

            buttonHideLayer.setImageDrawable(
                if (layer.opacity == 0f) {
                    eyeCloseDrawable
                } else {
                    eyeDrawable
                }
            )
        }
    }

    fun submitList(list: List<Layer>?) {
        layersRecyclerViewAdapter.submitList(list)
    }

    fun refreshItem(index: Int) {
        layersRecyclerViewAdapter.notifyItemChanged(index)
    }

    fun refreshList() {
        layersRecyclerViewAdapter.notifyDataSetChanged()
    }
}