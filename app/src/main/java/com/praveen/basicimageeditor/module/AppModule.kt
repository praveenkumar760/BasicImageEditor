package com.praveen.basicimageeditor.module

import com.praveen.basicimageeditor.viewmodel.BaseViewModel
import com.praveen.basicimageeditor.viewmodel.HomeViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { HomeViewModel() }
    viewModel { BaseViewModel() }
}