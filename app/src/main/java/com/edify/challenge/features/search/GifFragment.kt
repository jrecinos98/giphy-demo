package com.edify.challenge.features.search

import android.app.Activity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnTouchListener
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.edify.challenge.App
import com.edify.challenge.AppConstants
import com.edify.challenge.ViewModelFactory
import com.edify.challenge.databinding.FragmentGifBinding
import com.giphy.sdk.core.models.Media
import com.giphy.sdk.core.models.enums.RatingType
import com.giphy.sdk.ui.drawables.ImageFormat
import com.giphy.sdk.ui.pagination.GPHContent
import com.giphy.sdk.ui.utils.uriWithFormat
import com.giphy.sdk.ui.utils.videoUrl
import com.giphy.sdk.ui.views.GPHGridCallback
import com.giphy.sdk.ui.views.GPHSearchGridCallback
import com.giphy.sdk.ui.views.GifView
import timber.log.Timber
import javax.inject.Inject


/**
 * Displays Trending and Searched GIFs
 */
class GifFragment : Fragment() {

    private var _binding: FragmentGifBinding? = null

    private val binding get() = _binding!!

    @Inject
    lateinit var viewModelFactory: ViewModelFactory<GifViewModel>
    private val viewModel: GifViewModel by lazy {
        viewModelFactory.get<GifViewModel>(
            requireActivity()
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        App.getAppComponent().inject(this)
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentGifBinding.inflate(inflater, container, false)
        initGiphyGrid()
        initSearch()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeSearchTerm()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initSearch(){
        binding.searchBtn.setOnClickListener {
            viewModel.searchGif(binding.searchField.text.toString())
        }

        binding.searchField.apply{
            //Sets listener to the keyboard editor action
            setOnEditorActionListener { v: TextView?, actionId: Int, event: KeyEvent?->
                when(actionId){
                    EditorInfo.IME_ACTION_GO, EditorInfo.IME_NULL, EditorInfo.IME_ACTION_SEARCH -> {
                        dismissKeyboard()
                        viewModel.searchGif(v?.text.toString())
                        return@setOnEditorActionListener true
                    }
                }
                false
            }

            addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(p0: Editable?) = Unit
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) = Unit
                override fun onTextChanged(text: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    viewModel.searchGif(text.toString())
                }
            })

            //Sets the keyboard editor action to the GO state (so we can search)
            imeOptions = EditorInfo.IME_ACTION_GO
        }

        binding.searchCancel.setOnClickListener {
            //Reset text field to empty string
            binding.searchField.setText("")
        }
    }

    private fun initGiphyGrid(){
        binding.gifsGridView.apply {
            fixedSizeCells = AppConstants.GiphyConfig.fixedSizeCells
            //init grid with Trending Gifs until a search is performed
            setTrendingGifs()
            callback = object : GPHGridCallback {
                override fun contentDidUpdate(resultCount: Int) {}
                override fun didSelectMedia(media: Media) {
                    findNavController().navigate(GifFragmentDirections.actionGifSearchToGifPreview(media.id))
                }
            }
        }
    }

    private fun dismissKeyboard() {
        val imm = requireContext().getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(binding.searchField.windowToken, 0)
    }

    private fun observeSearchTerm(){
        viewModel.searchObs.observe(viewLifecycleOwner){ search ->
            if(search.isNotEmpty()){
                setGridContent(GPHContent.searchQuery(search, AppConstants.GiphyConfig.mediaType))
            }
            //If empty then reset to trending GIFs and dismiss keyboard
            else {
                dismissKeyboard()
                setTrendingGifs()
            }
        }
    }

    private fun setTrendingGifs(){
        setGridContent(GPHContent.trending(AppConstants.GiphyConfig.mediaType, RatingType.pg13))
    }

    private fun setGridContent(content : GPHContent){
        binding.gifsGridView.content = content
    }
}