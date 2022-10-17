package com.msmlabs.kmmnotes.android.notelist.composables

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.msmlabs.kmmnotes.android.NotesApplicationTheme
import com.msmlabs.kmmnotes.android.R
import com.msmlabs.kmmnotes.domain.note.Note
import com.msmlabs.kmmnotes.domain.time.DateTimeUtil

@Composable
fun NoteItem(
    note: Note,
    backgroundColor: Color,
    onNoteClickedListener: () -> Unit,
    onDeleteClickedListener: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val formattedDate = remember(note.created) {
        DateTimeUtil.formatNoteDate(note.created)
    }
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(5.dp))
            .background(backgroundColor)
            .clickable { onNoteClickedListener() }
            .padding(16.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = note.title,
                fontWeight = FontWeight.SemiBold,
                fontSize = 20.sp
            )
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = stringResource(R.string.delete_note),
                modifier = Modifier
                    .clickable(MutableInteractionSource(), null) { // removing the ripply effect
                        onDeleteClickedListener()
                    }
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = note.content, fontWeight = FontWeight.Light)
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = formattedDate, color = Color.DarkGray, modifier = Modifier.align(Alignment.End))
    }
}

@Preview(name = "Light Mode", showBackground = true)
@Preview(name = "Dark Mode", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun NoteItemPreview() {
    val note = Note(
        id = null,
        title = "Note Title",
        content = "This is the content",
        colorHex = 0,
        created = DateTimeUtil.now()
    )

    NotesApplicationTheme {
        Surface {
            NoteItem(
                note = note,
                backgroundColor = Color.Yellow,
                onNoteClickedListener = {},
                onDeleteClickedListener = {})
        }
    }
}
