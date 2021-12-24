package com.praveen.basicimageeditor.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.praveen.basicimageeditor.databinding.ItemImageEditorBinding
import com.praveen.basicimageeditor.editorinterface.HomeButtonClickListener
import com.praveen.basicimageeditor.utils.CommonUtils

class HomeAdapter(
    private var homeList: ArrayList<String>,
    private var mCallBack:HomeButtonClickListener
) : RecyclerView.Adapter<HomeAdapter.HomeViewHolder>() {

    class HomeViewHolder(private val binding: ItemImageEditorBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(homeList: ArrayList<String>,mCallBack: HomeButtonClickListener) {
            binding.mbEditor.text = homeList[adapterPosition]
            CommonUtils.setBgColorCornerRadius(
                binding.mbEditor,
                CommonUtils.setRandomColorForMaterialButton(binding.mbEditor)
            )
            binding.mbEditor.setOnClickListener {
                mCallBack.onButtonClick(homeList[adapterPosition])
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeViewHolder {
        return HomeViewHolder(
            ItemImageEditorBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: HomeViewHolder, position: Int) {
        if (!homeList.isNullOrEmpty()) {
            holder.bind(homeList,mCallBack)
        }
    }

    override fun getItemCount(): Int {
        return homeList.size
    }
}