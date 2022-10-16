package com.msmlabs.kmmnotes.domain.note

import com.msmlabs.kmmnotes.presentation.BabyBlueHex
import com.msmlabs.kmmnotes.presentation.LightGreenHex
import com.msmlabs.kmmnotes.presentation.RedOrangeHex
import com.msmlabs.kmmnotes.presentation.RedPinkHex
import com.msmlabs.kmmnotes.presentation.VioletHex
import kotlinx.datetime.LocalDateTime

data class Note(
    val id: Long?,
    val title: String,
    val content: String,
    val colorHex: Long,
    val created: LocalDateTime,
) {
    companion object {
        private val colors = listOf(RedOrangeHex, RedPinkHex, BabyBlueHex, VioletHex, LightGreenHex)
        fun generateRandomColor() = colors.random()
    }
}
