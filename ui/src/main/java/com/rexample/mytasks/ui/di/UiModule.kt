package com.rexample.mytasks.ui.di

import com.rexample.mytasks.ui.addnewtask.NewTaskViewModel
import com.rexample.mytasks.ui.edittask.EditTaskViewModel
import com.rexample.mytasks.ui.home.HomeViewModel
import com.rexample.mytasks.ui.managecategory.ManageCategoryViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val uiModule = module {
    viewModel{ HomeViewModel(get(), get()) }
    viewModel{ NewTaskViewModel(get(), get()) }
    viewModel { ManageCategoryViewModel(get()) }
    viewModel { EditTaskViewModel(get(), get()) }
}