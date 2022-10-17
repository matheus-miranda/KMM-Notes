package com.msmlabs.kmmnotes.android.notelist

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.msmlabs.kmmnotes.android.TIMEOUT_FIVE_SECONDS
import com.msmlabs.kmmnotes.domain.note.Note
import com.msmlabs.kmmnotes.domain.note.NoteDataSource
import com.msmlabs.kmmnotes.domain.note.SearchNoteUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val NOTES_KEY = "notes"
private const val SEARCH_TEXT_KEY = "searchText"
private const val SEARCH_ACTIVE_KEY = "searchActive"

@HiltViewModel
class NoteListViewModel @Inject constructor(
    private val noteDataSource: NoteDataSource,
    private val savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val searchNotesUseCase = SearchNoteUseCase()

    private val notes = savedStateHandle.getStateFlow(NOTES_KEY, emptyList<Note>())
    private val searchText = savedStateHandle.getStateFlow(SEARCH_TEXT_KEY, "")
    private val isSearchActive = savedStateHandle.getStateFlow(SEARCH_ACTIVE_KEY, false)

    val state = combine(notes, searchText, isSearchActive) { notes, searchText, isSearchActive ->
        NoteListState(
            notes = searchNotesUseCase(notes, searchText),
            searchText = searchText,
            isSearchActive = isSearchActive
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(TIMEOUT_FIVE_SECONDS), NoteListState())

    fun loadNotes() {
        viewModelScope.launch {
            savedStateHandle[NOTES_KEY] = noteDataSource.getAllNotes()
        }
    }

    fun onSearchTextChange(text: String) {
        savedStateHandle[SEARCH_TEXT_KEY] = text
    }

    fun onToggleSearch() {
        savedStateHandle[SEARCH_ACTIVE_KEY] = !isSearchActive.value
        if (!isSearchActive.value) {
            savedStateHandle[SEARCH_TEXT_KEY] = ""
        }
    }

    fun deleteNoteById(id: Long) {
        viewModelScope.launch {
            noteDataSource.deleteNoteById(id)
            loadNotes() // must reload notes again since we're not using Flow
        }
    }
}
