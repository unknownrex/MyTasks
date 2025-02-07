package com.rexample.mytasks.ui.di

import com.rexample.mytasks.AppViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module { viewModel{ AppViewModel(get()) } }