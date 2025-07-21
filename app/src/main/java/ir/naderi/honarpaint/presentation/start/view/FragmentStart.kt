package ir.naderi.honarpaint.presentation.start.view

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import ir.naderi.honarpaint.R
import ir.naderi.honarpaint.databinding.FragmentStartBinding
import ir.naderi.honarpaint.presentation.start.viewmodel.StartViewModel

@AndroidEntryPoint
class FragmentStart : Fragment(R.layout.fragment_start) {
    private var _binding: FragmentStartBinding? = null
    private val binding: FragmentStartBinding
        get() = _binding!!

    private val viewModel: StartViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentStartBinding.bind(view)

        binding.buttonStart.setOnClickListener {
            viewModel.onStartClicked(
                binding.editTextWidth.text.toString(),
                binding.editTextHeight.text.toString()
            )
        }

        viewModel.navigateToPaint.observe(viewLifecycleOwner, Observer { dims ->
            if (dims != null) {

                findNavController().navigate(
                    FragmentStartDirections.actionStartFragmentToFragmentPaint(
                        dims.width,
                        dims.height
                    )
                )

                viewModel.onNavigationHandled()
            }
        })

        viewModel.toastMessage.observe(viewLifecycleOwner, Observer { msg ->
            msg?.let {
                Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT)
                    .show();
                viewModel.onToastShown()
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}