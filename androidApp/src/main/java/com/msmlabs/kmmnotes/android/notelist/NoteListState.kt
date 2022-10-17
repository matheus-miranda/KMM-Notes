package com.msmlabs.kmmnotes.android.notelist

import com.msmlabs.kmmnotes.domain.note.Note

data class NoteListState(
    val notes: List<Note> = emptyList(),
    val searchText: String = "",
    val isSearchActive: Boolean = false,
)
