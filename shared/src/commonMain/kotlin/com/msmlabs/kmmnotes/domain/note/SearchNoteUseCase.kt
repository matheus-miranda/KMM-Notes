package com.msmlabs.kmmnotes.domain.note

import com.msmlabs.kmmnotes.domain.time.DateTimeUtil

class SearchNoteUseCase {
    operator fun invoke(notes: List<Note>, query: String): List<Note> {
        if (query.isBlank()) return notes

        return notes.filter {
            it.title.trim().lowercase().contains(query.lowercase()) ||
                it.content.trim().lowercase().contains(query.lowercase())
        }.sortedBy {
            DateTimeUtil.toEpochMillis(it.created)
        }
    }
}
