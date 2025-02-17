package com.rexample.mytasks.ui.core.util

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import org.koin.android.ext.koin.androidContext
import org.koin.compose.KoinApplication
import org.koin.compose.KoinContext
import org.koin.core.module.Module
import org.koin.mp.KoinPlatformTools

@Composable
fun KoinPreviewWrapper(
    modules: List<Module>,
    content: @Composable () -> Unit
){
    val context = LocalContext.current
    val alreadyExists = KoinPlatformTools.defaultContext().getOrNull() != null
    if (!alreadyExists){
        KoinApplication(application = {
            androidContext(context)
            properties(mapOf("BASE_URL" to ""))
            properties(mapOf("API_VERSION" to ""))
            modules(modules)
        }) {
            content()
        }
    }else{
        KoinContext {
            content()
        }
    }
}