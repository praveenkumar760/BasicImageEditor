package com.praveen.basicimageeditor.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlin.collections.ArrayList

class HomeViewModel() : ViewModel() {

    private val _imageEditorList = MutableLiveData<ArrayList<String>>()
    val imageEditorList: LiveData<ArrayList<String>>
        get() = _imageEditorList

    fun setHomeButtonList(homeButtonList:ArrayList<String>){
        if (!homeButtonList.isNullOrEmpty()){
            _imageEditorList.value = homeButtonList
        }
    }
}