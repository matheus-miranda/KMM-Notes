package com.msmlabs.kmmnotes.android.notedetail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.msmlabs.kmmnotes.android.NOTE_ID_ARGUMENT
import com.msmlabs.kmmnotes.android.TIMEOUT_FIVE_SECONDS
import com.msmlabs.kmmnotes.domain.note.Note
import com.msmlabs.kmmnotes.domain.note.NoteDataSource
import com.msmlabs.kmmnotes.domain.time.DateTimeUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val TITLE_KEY = "noteTitle"
private const val TITLE_FOCUSED_KEY = "isNoteTitleFocused"
private const val CONTENT_KEY = "noteContent"
private const val CONTENT_FOCUSED_KEY = "isNoteContentFocused"
private const val COLOR_KEY = "noteColor"

@HiltViewModel
class NoteDetailViewModel @Inject constructor(
    private val noteDataSource: NoteDataSource,
    private val savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val noteTitle = savedStateHandle.getStateFlow(TITLE_KEY, "")
    private val isNoteTitleFocused = savedStateHandle.getStateFlow(TITLE_FOCUSED_KEY, false)
    private val noteContent = savedStateHandle.getStateFlow(CONTENT_KEY, "")
    private val isNoteContentFocused = savedStateHandle.getStateFlow(CONTENT_FOCUSED_KEY, false)
    private val noteColor = savedStateHandle.getStateFlow(COLOR_KEY, Note.generateRandomColor())

    val state = combine(
        noteTitle,
        isNoteTitleFocused,
        noteContent,
        isNoteContentFocused,
        noteColor
    ) { title, isTitleFocused, content, isContentFocused, color ->
        NoteDetailState(
            noteTitle = title,
            isNoteTitleHintVisible = title.isEmpty() && !isTitleFocused,
            noteContent = content,
            isNoteContentHintVisible = content.isEmpty() && !isContentFocused,
            noteColor = color
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(TIMEOUT_FIVE_SECONDS), NoteDetailState())

    private val _hasNoteBeenSaved = MutableStateFlow(false)
    val hasNoteBeenSaved = _hasNoteBeenSaved.asStateFlow()

    private var existingNoteId: Long? = null

    init { // load note if id already exists
        savedStateHandle.get<Long>(NOTE_ID_ARGUMENT)?.let { existingNoteId ->
            if (existingNoteId == -1L) {
                return@let
            }
            this.existingNoteId = existingNoteId
            viewModelScope.launch {
                noteDataSource.getNoteById(existingNoteId)?.let { note ->
                    savedStateHandle[TITLE_KEY] = note.title
                    savedStateHandle[CONTENT_KEY] = note.content
                    savedStateHandle[COLOR_KEY] = note.colorHex
                }
            }
        }
    }

    fun onNoteTitleChanged(text: String) {
        savedStateHandle[TITLE_KEY] = text
    }

    fun onNoteContentChanged(text: String) {
        savedStateHandle[CONTENT_KEY] = text
    }

    fun onNoteTitleFocusChanged(isFocused: Boolean) {
        savedStateHandle[TITLE_FOCUSED_KEY] = isFocused
    }

    fun onNoteContentFocusChanged(isFocused: Boolean) {
        savedStateHandle[CONTENT_FOCUSED_KEY] = isFocused
    }

    fun saveNote() {
        viewModelScope.launch {
            if (noteTitle.value.isNotBlank() || noteContent.value.isNotBlank()) {
                noteDataSource.insertNote(
                    Note(
                        id = existingNoteId,
                        title = noteTitle.value,
                        content = noteContent.value,
                        colorHex = noteColor.value,
                        created = DateTimeUtil.now()
                    )
                )
                _hasNoteBeenSaved.value = true
            }
        }
    }
}
