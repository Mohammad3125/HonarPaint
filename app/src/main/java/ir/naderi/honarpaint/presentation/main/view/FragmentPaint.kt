package ir.naderi.honarpaint.presentation.main.view

import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Build
import android.os.Bundle
import android.util.TypedValue
import android.view.View
import android.view.View.LAYER_TYPE_NONE
import android.view.View.LAYER_TYPE_SOFTWARE
import android.view.WindowManager
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.core.graphics.createBitmap
import androidx.core.view.get
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import ir.naderi.honarpaint.R
import ir.naderi.honarpaint.databinding.FragmentPaintBinding
import ir.naderi.honarpaint.databinding.LayoutLayerSettingBinding
import ir.naderi.honarpaint.presentation.main.view.managers.BrushExtraSettingManager
import ir.naderi.honarpaint.presentation.main.view.managers.BrushLayoutManager
import ir.naderi.honarpaint.presentation.main.view.managers.ColorLayoutManager
import ir.naderi.honarpaint.presentation.main.view.managers.LayerManager
import ir.naderi.honarpaint.presentation.main.view.managers.LayerSettingManager
import ir.naderi.honarpaint.presentation.main.view.managers.PaintToolsLayerManager
import ir.naderi.honarpaint.presentation.main.view.managers.TopLayoutManager
import ir.naderi.honarpaint.presentation.main.viewmodel.MainViewModel
import ir.naderi.honarpaint.presentation.shared.SharedViewModel
import ir.naderi.honarpaint.utils.extensions.checkDuplicateAndNavigate
import ir.naderi.honarpaint.utils.extensions.flipVisibility
import ir.naderi.honarpaint.utils.extensions.gone
import ir.naderi.honarpaint.utils.extensions.visible
import ir.simurgh.photolib.components.paint.painters.coloring.LassoColorPainter
import ir.simurgh.photolib.components.paint.painters.coloring.flood.FloodFillPainter
import ir.simurgh.photolib.components.paint.painters.coloring.flood.FloodFillScanline
import ir.simurgh.photolib.components.paint.painters.colorpicker.ColorDropper
import ir.simurgh.photolib.components.paint.painters.painter.PainterMessage
import ir.simurgh.photolib.components.paint.painters.painting.BrushPainter
import ir.simurgh.photolib.components.paint.painters.painting.brushes.Brush
import ir.simurgh.photolib.components.paint.painters.painting.brushes.NativeBrush
import ir.simurgh.photolib.components.paint.painters.painting.engines.CanvasDrawingEngine


@AndroidEntryPoint
class FragmentPaint : Fragment(R.layout.fragment_paint) {

    private lateinit var dialogGetBack: AlertDialog
    private lateinit var globalBrush: Brush

    private var _binding: FragmentPaintBinding? = null
    private val binding: FragmentPaintBinding
        get() = _binding!!


    // Managers.
    lateinit var paintToolsLayerManager: PaintToolsLayerManager

    lateinit var colorLayoutManager: ColorLayoutManager

    lateinit var layerManager: LayerManager

    lateinit var layerSettingManager: LayerSettingManager

    lateinit var topLayoutManager: TopLayoutManager

    lateinit var brushLayoutManager: BrushLayoutManager

    lateinit var brushExtraSettingManager: BrushExtraSettingManager

    // Painters.
    private val lassoColorPainter by lazy {
        LassoColorPainter(requireContext()).apply {
            this.lassoSmoothness = 0.8f
        }
    }

    private val brushPainter by lazy {
        BrushPainter(CanvasDrawingEngine()).apply {
            brush = NativeBrush(size = 20)
        }
    }

    private val floodFiller by lazy {
        FloodFillScanline()
    }

    private val floodFillPainter by lazy {
        FloodFillPainter().apply {
            setOnFloodFillRequest { bitmap, ex, ey ->
                mainViewModel.floodFill(bitmap, ex, ey, lastSelectedColor, floodFiller)
            }
        }
    }

    private val colorDropperTool by lazy {
        ColorDropper().apply {
            setOnLastColorDetected {
                lastSelectedColor = it
                brushPainter.brush?.color = it
                lassoColorPainter.fillingColor = it
                binding.paintView.painter = brushPainter
            }
        }
    }

    // Colors.
    private var colorOnSurface: Int = 0
    private var colorPrimary: Int = 0

    private var lastSelectedColor = Color.BLACK

    // Viewmodel.
    private val mainViewModel by activityViewModels<MainViewModel>()

    private val sharedViewModel by activityViewModels<SharedViewModel>()

    private var currentLayoutViewedOnBottomExtraOptions: View? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.apply {
            mainViewModel.setFirstPageBitmap(
                getInt("width"),
                getInt("height")
            )
        }

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentPaintBinding.bind(view)

        initializeColors()

        initializePainterView()

        initializeManagers()

        initializeObservables()

        initializeDialogs()
    }

    private fun initializeDialogs() {
        dialogGetBack =
            createDialog(
                getString(R.string.closing_the_project),
                getString(R.string.are_you_sure_you_want_to_close_the_project),
                getString(R.string.yes),
                getString(R.string.no),
                getString(R.string.cancel),
                { dialogGetBack.dismiss() },
                {
                    dialogGetBack.dismiss()
                },
                onConfirm = {
                    findNavController().popBackStack()
                }
            )
    }

    private fun initializeObservables() {

        mainViewModel.apply {
            addLayer.observe(viewLifecycleOwner) { isAllowed ->
                if (isAllowed) {
                    binding.paintView.addNewLayer()
                    onLayerAdded()
                }
            }

            firstPageBitmap.observe(viewLifecycleOwner) { layer ->
                layer?.let {
                    binding.paintView.addNewLayer(layer)
                    firstPageBitmapShown()
                }
            }

            brushPreviews.observe(viewLifecycleOwner) { previews ->
                brushLayoutManager.submitBrushes(previews)
            }

            isFloodFillDone.observe(viewLifecycleOwner) { isDone ->
                isDone?.let {
                    if (isDone) {
                        binding.paintView.invalidate()
                        binding.paintView.onSendMessage(PainterMessage.SAVE_HISTORY)
                        onFloodFilled()
                    }
                }
            }
            isDoingOperation.observe(viewLifecycleOwner) { isActive ->
                if (isActive != null) {
                    if (isActive) {
                        showProgress()
                    } else {
                        hideProgress()
                    }
                    onDoingOperationShown()
                }
            }
            currentBrush.observe(viewLifecycleOwner) { newBrush ->
                newBrush?.let {
                    globalBrush = it
                    brushPainter.brush = globalBrush
                    brushExtraSettingManager.globalBrush = globalBrush

                    if (binding.layoutBrushSettingsExtra.isVisible) {
                        brushExtraSettingManager.setSlidersAndPreview(globalBrush)
                    }
                }
            }
            isBrushPreviewExpanded.observe(viewLifecycleOwner) { isExpanded ->
                if (isExpanded) {
                    binding.layoutBrushSettingsExtra.flipVisibility()
                    brushExtraSettingManager.collapseSettings()
                    if (binding.layoutBrushSettingsExtra.isVisible) {
                        brushExtraSettingManager.setSlidersAndPreview(globalBrush)
                    }
                    onExpandShown()
                }
            }
            selectedLayer.observe(viewLifecycleOwner) { layer ->
                layer?.let {
                    layerSettingManager.changeLayerSettingValues(layer)
                    layerManager.changeLockedAndOpacityDrawablesWithLayer(layer)
                }
            }
            layers.observe(viewLifecycleOwner) { l ->
                layerManager.submitList(l)
            }
            removedLayers.observe(viewLifecycleOwner) { indices ->
                indices?.let {
                    binding.paintView.removeLayers(indices)
                    layerManager.isCheckMode = false
                    setPaintViewLayerType()
                    onRemovedLayerCalled()
                }
            }
            mergedLayer.observe(viewLifecycleOwner) { indices ->
                indices?.let {
                    binding.paintView.mergeLayers(indices)
                    layerManager.isCheckMode = false
                    onMergeLayersCalled()
                }
            }
            toastMessage.observe(viewLifecycleOwner) { message ->
                message?.let {
                    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                    onToastShown()
                }
            }
            appLimitsHolder.observe(viewLifecycleOwner) { limits ->
                binding.paintView.apply {
                    historyHandler.options.maximumHistorySize = limits.historySize
                    isCachingEnabled = limits.isCachingEnabled
                }
            }
        }


    }

    private fun initializeColors() {
        val typedValue = TypedValue()

        requireContext().theme.resolveAttribute(
            com.google.android.material.R.attr.colorOnSurface,
            typedValue, true
        )

        colorOnSurface = resources.getColor(typedValue.resourceId)

        requireContext().theme.resolveAttribute(
            com.google.android.material.R.attr.colorPrimary,
            typedValue, true
        )

        colorPrimary = resources.getColor(typedValue.resourceId)


    }

    private fun initializePainterView() {
        binding.paintView.apply {
            painter = brushPainter

            setOnLayersChangedListener { layers, selectedLayerIndex ->
                mainViewModel.onLayersChanged(layers, selectedLayerIndex)
            }

            setOnUndoOrRedoListener { isUndoEnabled, isRedoEnabled ->
                topLayoutManager.setUndoRedoState(isUndoEnabled, isRedoEnabled)
            }

        }

    }

    private fun initializeManagers() {
        binding.apply {

            paintToolsLayerManager = PaintToolsLayerManager(layoutPaintToolBinding).apply {
                onBrushButtonSelected = {
                    if (paintView.painter !== brushPainter || brushPainter.engine.isEraserModeEnabled()) {
                        hideBottomExtraOptionsLayer()

                        brushPainter.setEraserMode(false)

                        paintView.painter = brushPainter
                    } else if (!brushesLayoutBinding.layoutBrushesPreview.isVisible) {
                        showLayoutOnBottomExtraLayout(brushesLayoutBinding.layoutBrushesPreview)
                    } else {
                        hideBottomExtraOptionsLayer()
                    }
                }
                onEraserButtonSelected = {
                    hideBottomExtraOptionsLayer()
                    if (paintView.painter !== brushPainter || !brushPainter.engine.isEraserModeEnabled()) {

                        brushPainter.setEraserMode(true)

                        if (paintView.painter !== brushPainter) {
                            paintView.painter = brushPainter
                        }

                    } else if (!brushesLayoutBinding.layoutBrushesPreview.isVisible) {
                        showLayoutOnBottomExtraLayout(brushesLayoutBinding.layoutBrushesPreview)
                    } else {
                        hideBottomExtraOptionsLayer()
                    }
                }
                onColorButtonSelected = {
                    doIfNotReselectedBottomExtraLayout(colorHolderBinding.root) {
                        showLayoutOnBottomExtraLayout(colorHolderBinding.root)
                        colorLayoutManager.initializeLayout()
                    }
                }
                onFillButtonSelected = {
                    hideBottomExtraOptionsLayer()
                    paintView.painter = floodFillPainter
                }
                onLassoButtonSelected = {
                    hideBottomExtraOptionsLayer()

                    if (paintView.painter !== lassoColorPainter) {
                        paintView.painter = lassoColorPainter
                    }
                }
            }

            colorLayoutManager = ColorLayoutManager(colorHolderBinding).apply {
                onDialogShown = {

                }
                onDialogCancel = {

                }
                onDropperShown = {
                    paintView.painter = colorDropperTool
                }
                onPickColor = { pickedColor ->
                    brushPainter.brush?.color = pickedColor
                    lassoColorPainter.fillingColor = pickedColor
                    lastSelectedColor = pickedColor
                }
            }

            layerManager = LayerManager(layoutPainterLayersBinding).apply {
                onLayerAdded = {
                    mainViewModel.onAddLayerClicked(paintView.getLayerCount())
                }
                onLayerDeleted = { list ->
                    mainViewModel.onLayerDeleted(list, layerManager.isCheckMode)
                }
                onLayerDeletedLong = {
                    paintView.getSelectedLayerBitmap()?.apply {
                        eraseColor(Color.TRANSPARENT)
                        paintView.setSelectedLayerBitmap(this)
                    }
                }
                onLayerSelected = {
                    paintView.selectLayer(it)
                }
                onLayerHidden = {
                    val layerIndex = paintView.getSelectedLayerIndex()
                    if (paintView.getLayerOpacityAt(layerIndex) == 1f) {
                        paintView.setLayerOpacityAt(layerIndex, 0f)
                    } else {
                        paintView.setLayerOpacityAt(layerIndex, 1f)
                    }
                    layerManager.changeLockedAndOpacityDrawablesWithLayer(paintView.getPaintLayers()[layerIndex])

                    layerManager.refreshItem(layerIndex)
                    layerManager.isCheckMode = false
                }
                /* onLayerTransformed = {
                     paintView.getSelectedLayerBitmap()?.let { layerBitmap ->
                         *//* mainViewModel.onLayerTransformClicked(layerBitmap)
                         isLayerTransforming = true
                         showProgress()*//*
                    }
                }*/
                onLayerUp = {
                    paintView.moveSelectedLayerUp()
                }
                onLayerDown = {
                    paintView.moveSelectedLayerDown()
                }
                onLayerCopied = {
                    paintView.duplicateSelectedLayer()
                }
                onLayerMerged = { list ->
                    mainViewModel.onMergeLayouts(list)
                }
                onLayerSetting = {
                    layerSettingManager.changeLayerSettingValues(paintView.getPaintLayers()[paintView.getSelectedLayerIndex()])
                    layerSettingManager.show()
                }
                onLayerLocked = {
                    val layerIndex = paintView.getSelectedLayerIndex()

                    if (paintView.isLayerLockedAt(layerIndex)) {
                        paintView.setLayerLockedStateAt(layerIndex, false)
                    } else {
                        paintView.setLayerLockedStateAt(layerIndex, true)
                    }

                    layerManager.apply {
                        changeLockedAndOpacityDrawablesWithLayer(paintView.getPaintLayers()[layerIndex])
                        refreshItem(layerIndex)
                        isCheckMode = false
                    }
                }
            }

            layerSettingManager =
                LayerSettingManager(LayoutLayerSettingBinding.inflate(layoutInflater)).apply {
                    onBlendingModeItemClicked = { selectedItem ->
                        val finalBlendingMode =
                            if (selectedItem == "NORMAL") {
                                paintView.setLayerType(LAYER_TYPE_NONE, null)
                                PorterDuff.Mode.SRC
                            } else {
                                checkBlendingModeSoftwareLayer(selectedItem)

                                PorterDuff.Mode.valueOf(selectedItem)
                            }

                        paintView.setSelectedLayerBlendingMode(finalBlendingMode)
                    }
                    onSliderValueEnded = { value ->
                        if (!isSelectedLayerLocked()) {
                            paintView.setLayerOpacityAt(
                                paintView.getSelectedLayerIndex(),
                                value / 100f
                            )
                        }
                    }
                    onSliderValueChanged = { value ->
                        if (!isSelectedLayerLocked()) {
                            paintView.changeLayerOpacityAtWithoutStateSave(
                                paintView.getSelectedLayerIndex(),
                                value / 100f
                            )
                        }
                    }
                }

            topLayoutManager = TopLayoutManager(layoutMainTopBinding).apply {
                onUndoClicked = {
                    paintView.undo()
                }
                onRedoClicked = {
                    paintView.redo()
                }
                onResetMatrixClicked = {
                    paintView.resetTransformationMatrix()
                }
                onGetBackClicked = {
                    dialogGetBack.show()
                }
                onLayersClicked = {
                    cardViewLayers.flipVisibility()

                    setButtonLayersColor(if (cardViewLayers.isVisible) colorPrimary else colorOnSurface)

                    layerManager.isCheckMode = false
                }
                onSaveFinalImageClicked = {
                    paintView.convertToBitmap()?.let {
                        sharedViewModel.selectBitmap(it)
                        findNavController().checkDuplicateAndNavigate(
                            FragmentPaintDirections.actionFragmentPaintToFragmentExport(),
                            R.id.fragmentPaint
                        )
                    }
                }

            }

            brushLayoutManager = BrushLayoutManager(brushesLayoutBinding).apply {
                onBrushItemClicked = { brushPreview ->
                    mainViewModel.onBrushClicked(brushPreview)
                }
            }

            brushExtraSettingManager = BrushExtraSettingManager(layoutInSettings).apply {
                onCollapse = {
                    binding.layoutInSettings.buttonExpandBrushSettings.postDelayed({
                        binding.layoutBrushSettingsExtra.gone()
                    }, 100)
                }
            }

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun showLayoutOnBottomExtraLayout(targetLayout: View) {
        binding.layoutMainExtraOptions.visible()
        hideBottomExtraOptionsLayer()
        targetLayout.visible()
        currentLayoutViewedOnBottomExtraOptions = targetLayout
    }

    private fun hideBottomExtraOptionsLayer() {
        currentLayoutViewedOnBottomExtraOptions?.gone()
        binding.layoutBrushSettingsExtra.gone()
    }

    private inline fun doIfNotReselectedBottomExtraLayout(layout: View, job: () -> Unit) {
        if (currentLayoutViewedOnBottomExtraOptions !== layout || currentLayoutViewedOnBottomExtraOptions?.isGone == true) {
            job.invoke()
        } else {
            hideBottomExtraOptionsLayer()
        }
    }

    private fun LinearLayoutCompat.setSingleItemColor(color: Int) {
        (get(0) as AppCompatImageView).imageTintList =
            ColorStateList.valueOf(color)
        (get(1) as TextView).setTextColor(color)
    }

    private fun setPaintViewLayerType() {
        binding.apply {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.P && paintView.layerType == LAYER_TYPE_NONE && paintView.isAnyLayerBlending()) {
                paintView.setLayerType(LAYER_TYPE_SOFTWARE, null)
            } else {
                paintView.setLayerType(LAYER_TYPE_NONE, null)
            }
        }
    }

    private fun checkBlendingModeSoftwareLayer(text: String) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.P &&
            (text == "OVERLAY" || text == "DARKEN" || text == "LIGHTEN") &&
            binding.paintView.layerType == LAYER_TYPE_NONE
        ) {
            binding.paintView.setLayerType(LAYER_TYPE_SOFTWARE, null)
        }
    }

    private fun isSelectedLayerLocked(): Boolean {
        return if (binding.paintView.getSelectedLayerLockState()) {
            Toast.makeText(context, getString(R.string.layer_is_locked), Toast.LENGTH_SHORT).show()
            true
        } else {
            false
        }
    }

    fun showProgress() {
        binding.apply {
            frameLayoutProgressBar.visible()
            circularProgressIndicator.show()
        }
    }

    fun hideProgress() {
        binding.apply {
            frameLayoutProgressBar.gone()
            circularProgressIndicator.hide()
        }
    }

    private fun createDialog(
        message: String,
        description: String,
        confirmText: String,
        discardText: String,
        neutralText: String,
        onNeutral: () -> Unit,
        onDiscard: () -> Unit,
        onConfirm: () -> Unit
    ): AlertDialog {
        return MaterialAlertDialogBuilder(requireContext())
            .setMessage(description)
            .setNegativeButton(discardText) { _, _ ->
                onDiscard.invoke()
            }
            .setNeutralButton(neutralText) { _, _ ->
                onNeutral.invoke()
            }
            .setOnDismissListener { onNeutral.invoke() }
            .setTitle(message)
            .setPositiveButton(confirmText) { _, _ ->
                onConfirm.invoke()
            }.create()
    }


}