package com.rexample.mytasks.ui.core.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.rexample.mytasks.ui.R

val poppins = FontFamily(
    listOf(
        Font(R.font.poppins_regular, FontWeight.Normal),
        Font(R.font.poppins_semibold, FontWeight.SemiBold),
        Font(R.font.poppins_medium, FontWeight.Medium),
        Font(R.font.poppins_bold, FontWeight.Bold),
    )

)

val bodyFontFamily = poppins

val baseline = Typography()

// Set of Material typography styles to start with
val AppTypography = Typography(
    displayLarge = baseline.displayLarge.copy(fontFamily = bodyFontFamily),
    displayMedium = baseline.displayMedium.copy(fontFamily = bodyFontFamily),
    displaySmall = baseline.displaySmall.copy(fontFamily = bodyFontFamily),
    headlineLarge = baseline.headlineLarge.copy(fontFamily = bodyFontFamily),
    headlineMedium = baseline.headlineMedium.copy(fontFamily = bodyFontFamily),
    headlineSmall = baseline.headlineSmall.copy(fontFamily = bodyFontFamily),
    titleLarge = baseline.titleLarge.copy(fontFamily = bodyFontFamily),
    titleMedium = baseline.titleMedium.copy(fontFamily = bodyFontFamily),
    titleSmall = baseline.titleSmall.copy(fontFamily = bodyFontFamily),
    bodyLarge = baseline.bodyLarge.copy(fontFamily = bodyFontFamily),
    bodyMedium = baseline.bodyMedium.copy(fontFamily = bodyFontFamily),
    bodySmall = baseline.bodySmall.copy(fontFamily = bodyFontFamily),
    labelLarge = baseline.labelLarge.copy(fontFamily = bodyFontFamily),
    labelMedium = baseline.labelMedium.copy(fontFamily = bodyFontFamily),
    labelSmall = baseline.labelSmall.copy(fontFamily = bodyFontFamily),
)