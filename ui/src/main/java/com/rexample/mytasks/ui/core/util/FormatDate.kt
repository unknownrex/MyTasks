package com.rexample.mytasks.ui.core.util

import java.time.LocalDate
import java.time.format.DateTimeFormatter

fun formatDate(dateString: String?): String {
    if (dateString.isNullOrEmpty()) return "Tidak ada tanggal"

    val formatter = DateTimeFormatter.ofPattern("dd - MM - yyyy")
    val taskDate = LocalDate.parse(dateString, formatter)
    val today = LocalDate.now()

    return when {
        taskDate == today -> "Hari ini"
        taskDate.year == today.year -> taskDate.format(DateTimeFormatter.ofPattern("dd - MM"))
        else -> taskDate.format(formatter)
    }
}
