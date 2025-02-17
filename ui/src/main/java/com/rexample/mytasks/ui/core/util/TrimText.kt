package com.rexample.mytasks.ui.core.util

import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.LineHeightStyle


fun trimText(
    alignment: LineHeightStyle.Alignment = LineHeightStyle.Alignment.Center,
    trim: LineHeightStyle.Trim = LineHeightStyle.Trim.Both
): TextStyle {
    return TextStyle.Default.copy(
        lineHeightStyle = LineHeightStyle(
            alignment = alignment,
            trim = trim
        )
    )
}
