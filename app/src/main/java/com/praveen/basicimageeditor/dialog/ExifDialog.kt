package com.praveen.basicimageeditor.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.praveen.basicimageeditor.R
import com.praveen.basicimageeditor.databinding.DialogExifBinding
import com.praveen.basicimageeditor.utils.Constants

class ExifDialog : BottomSheetDialogFragment() {
    private lateinit var binding: DialogExifBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.dialog_exif, container, false)
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NORMAL, R.style.ThemeOverlay_ImageEditor_BottomSheet)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (arguments?.isEmpty == false) {
            binding.tvResolutionVal.text = arguments?.getString(Constants.RESOLUTION)
            binding.tvOrientationVal.text = arguments?.getString(Constants.ORIENTATION)
        }
        binding.close.setOnClickListener {
            dismiss()
        }
    }
}