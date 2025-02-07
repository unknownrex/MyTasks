package com.rexample.mytasks.ui.di

import com.rexample.mytasks.ui.login.LoginViewModel
import com.rexample.mytasks.ui.register.RegisterViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val uiModule = module {
    viewModel{ RegisterViewModel(get()) }
    viewModel{ LoginViewModel(get()) }
}