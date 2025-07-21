package ir.naderi.honarpaint.presentation.export.view

import android.app.Dialog
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.addCallback
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.chip.Chip
import dagger.hilt.android.AndroidEntryPoint
import ir.naderi.honarpaint.R
import ir.naderi.honarpaint.databinding.FragmentExportBinding
import ir.naderi.honarpaint.presentation.export.viewmodel.ExportViewModel
import ir.naderi.honarpaint.presentation.shared.SharedViewModel

@AndroidEntryPoint
class FragmentExport : DialogFragment(R.layout.fragment_export) {
    private var binding: FragmentExportBinding? = null

    private lateinit var saveAsActivityLauncher: ActivityResultLauncher<Intent>

    private val sharedViewModel by activityViewModels<SharedViewModel>()
    private val exportViewModel by viewModels<ExportViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.fullscreenDialog)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return super.onCreateDialog(savedInstanceState).also { createdDialog ->
            createdDialog.window?.apply {
                setWindowAnimations(R.style.dialogFragmentAnimationStyle)
            }
            activity?.onBackPressedDispatcher?.addCallback(this) {
                findNavController().popBackStack()
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentExportBinding.bind(view).apply {

            exportViewModel.apply {

                exportData.observe(viewLifecycleOwner) { data ->
                    imageViewExportPreview.setImageBitmap(data.bitmap)
                    imageDimensionTextView.text = data.dimension
                }

                isQualitySliderEnabled.observe(viewLifecycleOwner) { isEnabled ->
                    sliderExportQuality.isEnabled = isEnabled
                }

                toastMessage.observe(viewLifecycleOwner) { message ->
                    message?.let {
                        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                        onToastShown()
                    }
                }

                onSavedSuccessfully.observe(viewLifecycleOwner) { isSuccessful ->
                    if (isSuccessful) {
                        findNavController().popBackStack()
                    }
                }
            }

            chipGroupFileExportType.setOnCheckedStateChangeListener { group, _ ->
                val chipText = root.findViewById<Chip>(group.checkedChipId).text
                exportViewModel.onExportTypeSelected(chipText)
            }

            sliderExportQuality.addOnChangeListener { _, value, _ ->
                exportViewModel.onExportQualityChanged(value.toInt())
            }

            buttonGetBackFromExport.setOnClickListener {
                findNavController().popBackStack()
            }

            sharedViewModel.selectedBitmap.observe(viewLifecycleOwner) { bitmap ->
                if (bitmap != null) {
                    exportViewModel.setExportBitmap(bitmap)
                    sharedViewModel.onSelectedBitmapShown()
                }
            }

            buttonExportImage.setOnClickListener {
                saveAsActivityLauncher.launch(Intent(Intent.ACTION_CREATE_DOCUMENT).apply {
                    type = "image/*"
                    addCategory(Intent.CATEGORY_OPENABLE)
                    putExtra(
                        Intent.EXTRA_TITLE,
                        exportViewModel.getDefaultFileName(getString(R.string.export_file_name))
                    )
                })
            }

            saveAsActivityLauncher =
                registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                    exportViewModel.onSave(result)
                }

        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}