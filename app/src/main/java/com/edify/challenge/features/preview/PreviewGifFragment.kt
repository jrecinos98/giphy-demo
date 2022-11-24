package com.edify.challenge.features.preview

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.edify.challenge.App
import com.edify.challenge.R
import com.edify.challenge.ViewModelFactory
import com.edify.challenge.databinding.FragmentPreviewGifBinding
import com.giphy.sdk.core.models.Media
import timber.log.Timber
import javax.inject.Inject


/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class PreviewGifFragment : DialogFragment() {

    private var _binding: FragmentPreviewGifBinding? = null

    private val binding get() = _binding!!

    private val args : PreviewGifFragmentArgs by navArgs()

    companion object{
        private const val PERMISSION_CODE = 10
    }

    @Inject
    lateinit var viewModelFactory: ViewModelFactory<PreviewViewModel>
    private val viewModel: PreviewViewModel by lazy {
        viewModelFactory.get<PreviewViewModel>(
            requireActivity()
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        App.getAppComponent().inject(this)
        setStyle(STYLE_NO_FRAME, R.style.Dialog)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPreviewGifBinding.inflate(inflater, container, false)
        observeSaveMessage()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dialog?.window?.attributes?.windowAnimations = R.style.Dialog
        binding.gifView.apply{
            setMediaWithId(args.id)
        }

        binding.saveButton.setOnClickListener {
            if (binding.gifView.media != null) {
                saveGif(binding.gifView.media!!)
            }
        }
    }

    private fun observeSaveMessage(){
        viewModel.errorObs.observe(viewLifecycleOwner){
            Toast.makeText(requireContext(),resources.getText(it), Toast.LENGTH_SHORT ).show()
        }
    }

    fun saveGif(media: Media) {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissions(
                arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                PERMISSION_CODE
            )
        } else {
            viewModel.saveGif(binding.gifView.media?.images?.original?.gifUrl ?: "", binding.gifView.mediaId!!)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == PERMISSION_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                viewModel.saveGif(binding.gifView.media?.images?.original?.gifUrl ?: "", binding.gifView.mediaId!!)
            } else {
                // Permission Denied
                Toast.makeText(requireContext(), "Permission Denied", Toast.LENGTH_SHORT).show()
            }
            return
        }
        super.onRequestPermissionsResult(requestCode, permissions!!, grantResults)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}