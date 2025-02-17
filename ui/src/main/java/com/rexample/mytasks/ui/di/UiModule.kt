package com.rexample.mytasks.ui.di

import com.rexample.mytasks.ui.addnewtask.NewTaskViewModel
import com.rexample.mytasks.ui.edittask.EditTaskViewModel
import com.rexample.mytasks.ui.home.HomeViewModel
import com.rexample.mytasks.ui.login.LoginViewModel
import com.rexample.mytasks.ui.managecategory.ManageCategoryViewModel
import com.rexample.mytasks.ui.profile.ProfileViewModel
import com.rexample.mytasks.ui.register.RegisterViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val uiModule = module {
    viewModel{ RegisterViewModel(get()) }
    viewModel{ LoginViewModel(get()) }
    viewModel{ HomeViewModel(get(), get(), get()) }
    viewModel{ NewTaskViewModel(get(), get(), get()) }
    viewModel { ManageCategoryViewModel(get(), get()) }
    viewModel { ProfileViewModel(get(), get(), get()) }
    viewModel { EditTaskViewModel(get(), get(), get()) }
}