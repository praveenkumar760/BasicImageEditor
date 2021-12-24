package com.praveen.basicimageeditor.fragment

import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.praveen.basicimageeditor.R
import com.praveen.basicimageeditor.adapter.HomeAdapter
import com.praveen.basicimageeditor.databinding.FragmentHomeBinding
import com.praveen.basicimageeditor.editorinterface.HomeButtonClickListener
import com.praveen.basicimageeditor.utils.Constants
import com.praveen.basicimageeditor.viewmodel.HomeViewModel
import com.theartofdev.edmodo.cropper.CropImage
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File
import java.io.IOException
import java.io.InputStream
import android.provider.OpenableColumns
import androidx.exifinterface.media.ExifInterface
import com.praveen.basicimageeditor.dialog.ExifDialog
import com.praveen.basicimageeditor.extensions.KotlinExtension.toast


class HomeFragment : Fragment(), HomeButtonClickListener {

    private val mTag = "HomeFragment"
    private lateinit var bindingHome: FragmentHomeBinding
    private val homeViewModel: HomeViewModel by viewModel()
    private var isExif = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        bindingHome = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)
        bindingHome.lifecycleOwner = this
        return bindingHome.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindingHome.viewmodel = homeViewModel
        homeViewModel.setHomeButtonList(
            resources.getStringArray(R.array.home_button_list).toList() as ArrayList<String>
        )
        initRecyclerView()
    }

    private fun initRecyclerView() {
        try {
            homeViewModel.imageEditorList.observe(viewLifecycleOwner, {
                if (!it.isNullOrEmpty()) {
                    val homeAdapter = HomeAdapter(it, this)
                    bindingHome.rvImageEditor.adapter = homeAdapter
                }
            })
        } catch (e: Exception) {
            Log.d(mTag, "initRecyclerView:: ${e.printStackTrace()}")
        }
    }

    private val openGallery =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            run {
                if (result.resultCode == RESULT_OK) {
                    result.data?.let { openImageEditorFragment(result.data?.data) }
                }
            }
        }

    private var cropImage = object : ActivityResultContract<Any?, Uri?>() {
        override fun createIntent(context: Context, input: Any?): Intent {
            return CropImage.activity().setAspectRatio(16, 9).getIntent(context)
        }

        override fun parseResult(resultCode: Int, intent: Intent?): Uri? {
            return CropImage.getActivityResult(intent)?.uri
        }
    }

    private var cropActivity = registerForActivityResult(cropImage) {
        it?.let {
            openImageEditorFragment(it)
        }
    }

    override fun onButtonClick(buttonName: String) {
        when (buttonName) {
            "Open Gallery" -> {
                isExif = false
                val intent =
                    Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
                openGallery.launch(intent)
            }
            "Crop Photo" -> {
                isExif = false
                cropActivity.launch(null)
            }
            "Exif Info" -> {
                isExif = true
                val intent =
                    Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
                openGallery.launch(intent)
            }
            else -> {
                context?.toast(resources.getString(R.string.future_implementation))
            }
        }
    }

    private fun openImageEditorFragment(uri: Uri?) {
        try {
            if (isExif) {
                extractExif(uri)
            } else {
                val fragment = ImageEditorFragment()
                val bundle = Bundle()
                bundle.putParcelable(Constants.IMAGE_URI, uri)
                fragment.arguments = bundle
                activity?.supportFragmentManager?.beginTransaction()
                    ?.replace(R.id.container, fragment)?.addToBackStack(null)?.commit()
            }
        } catch (e: Exception) {
            Log.d(mTag, "initHomeFragment${e.printStackTrace()}")
        }
    }

    private fun extractExif(uri: Uri?) {
        var inputStream: InputStream
        try {
            inputStream = uri?.let { context?.contentResolver?.openInputStream(it) }!!
            val exifInterface = ExifInterface(inputStream)
            val width = exifInterface.getAttribute(ExifInterface.TAG_IMAGE_WIDTH)
            val height = exifInterface.getAttribute(ExifInterface.TAG_IMAGE_LENGTH)
            val resolution = "$width * $height"
            val orientation = exifInterface.getAttribute(ExifInterface.TAG_ORIENTATION)
            val dialog = ExifDialog()
            dialog.isCancelable = false
            val bundle = Bundle()
            bundle.putString(Constants.RESOLUTION, resolution)
            bundle.putString(Constants.ORIENTATION, orientation)
            dialog.arguments = bundle
            dialog.show(requireActivity().supportFragmentManager, ExifDialog::class.java.name)
        } catch (e: IOException) {
            Log.d(mTag, "extractExif${e.printStackTrace()}")
        } finally {
            try {
                inputStream = uri?.let { context?.contentResolver?.openInputStream(it) }!!
                inputStream.close()
            } catch (ignored: IOException) {
                Log.d(mTag, "inputStreamClose${ignored.printStackTrace()}")
            }
        }
    }

}