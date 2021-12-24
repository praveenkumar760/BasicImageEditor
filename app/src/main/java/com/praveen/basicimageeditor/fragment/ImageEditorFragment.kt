package com.praveen.basicimageeditor.fragment

import android.content.ContentValues
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.praveen.basicimageeditor.R
import com.praveen.basicimageeditor.databinding.FragmentImageEditorBinding
import com.praveen.basicimageeditor.extensions.KotlinExtension.toast
import com.praveen.basicimageeditor.utils.Constants
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream
import java.lang.Exception
import android.graphics.ImageDecoder
import android.graphics.Matrix
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView


class ImageEditorFragment : Fragment(), View.OnClickListener {
    private val mTag = "ImageEditorFragment"
    private lateinit var imageBinding: FragmentImageEditorBinding
    private lateinit var mImageUri: Uri
    private var isRotateX = true
    private var isRotateY = true
    private lateinit var bitmap: Bitmap

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        imageBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_image_editor, container, false)
        return imageBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (arguments != null && arguments?.isEmpty == false) {
            mImageUri = arguments?.getParcelable(Constants.IMAGE_URI)!!
            setImage()
            setButtonClick()
        }
    }

    private fun setImage() {
        try {
            bitmap = convertUriToBitmap(mImageUri)
            imageBinding.ivImage.setImageBitmap(bitmap)
        } catch (e: Exception) {
            Log.d(mTag, "setImage:: ${e.printStackTrace()}")
        }
    }

    private fun setButtonClick() {
        imageBinding.ivBack.setOnClickListener(this)
        imageBinding.tvSave.setOnClickListener(this)
        imageBinding.tvCrop.setOnClickListener(null)
        imageBinding.tvFlipX.setOnClickListener(this)
        imageBinding.tvFlipY.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.iv_back -> {
                activity?.supportFragmentManager?.popBackStack()
            }
            R.id.tv_save -> {
                try {
                    saveMediaToStorage(bitmap)
                } catch (e: Exception) {
                    Log.d(mTag, "saveClick:: ${e.printStackTrace()}")
                }
            }
            R.id.tv_flip_x -> {
                imageBinding.ivImage.post {
                    if (isRotateX) {
                        imageBinding.ivImage.setImageBitmap(
                            flipImage(
                                bitmap,
                                1f,
                                -1f
                            )
                        )
                        isRotateX = false
                    } else {
                        imageBinding.ivImage.setImageBitmap(
                            flipImage(
                                bitmap,
                                1f,
                                1f
                            )
                        )
                        isRotateX = true
                    }
                }
            }
            R.id.tv_flip_y -> {
                imageBinding.ivImage.post {
                    if (isRotateY) {
                        imageBinding.ivImage.setImageBitmap(
                            flipImage(
                                bitmap,
                                -1f,
                                1f
                            )
                        )
                        isRotateY = false
                    } else {
                        imageBinding.ivImage.setImageBitmap(
                            flipImage(
                                bitmap,
                                1f,
                                1f
                            )
                        )
                        isRotateY = true
                    }
                }
            }
            R.id.tv_crop -> {
                CropImage.activity()
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .setCropShape(CropImageView.CropShape.RECTANGLE).setAspectRatio(1920, 1090)
                    .setOutputUri(mImageUri)
                    .start(requireActivity())
            }
        }
    }

    private fun flipImage(view: Bitmap, xVal: Float, yVal: Float): Bitmap {
        val matrix = Matrix()
        val width = view.width * 0.5f
        val height = view.height * 0.5f
        matrix.postScale(xVal, yVal, width, height)
        bitmap = Bitmap.createBitmap(view, 0, 0, view.width, view.height, matrix, true)
        return bitmap
    }

    private fun convertUriToBitmap(uri: Uri): Bitmap {
        val uriToBitmap: Bitmap?
        return if (Build.VERSION.SDK_INT >= 29) {
            val bitmap = context?.contentResolver?.let { ImageDecoder.createSource(it, uri) }
            uriToBitmap = ImageDecoder.decodeBitmap(bitmap!!)
            uriToBitmap
        } else {
            uriToBitmap = MediaStore.Images.Media.getBitmap(context?.contentResolver, uri)
            uriToBitmap
        }
    }

    private fun saveMediaToStorage(bitmap: Bitmap) {
        val filename = "${System.currentTimeMillis()}.jpg"
        var fos: OutputStream? = null
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            context?.contentResolver?.also { resolver ->
                val contentValues = ContentValues().apply {
                    put(MediaStore.MediaColumns.DISPLAY_NAME, filename)
                    put(MediaStore.MediaColumns.MIME_TYPE, "image/jpg")
                    put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES)
                }
                val imageUri: Uri? =
                    resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
                fos = imageUri?.let { resolver.openOutputStream(it) }
            }
        } else {
            val imagesDir =
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
            val image = File(imagesDir, filename)
            fos = FileOutputStream(image)
        }
        fos?.use {
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, it)
            context?.toast(resources.getString(R.string.success))
        }
    }
}