package com.msmlabs.kmmnotes.android.notelist.composables

import android.content.res.Configuration
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.msmlabs.kmmnotes.android.NotesApplicationTheme
import com.msmlabs.kmmnotes.android.R

@Composable
fun HideableSearchTextField(
    text: String,
    isSearchActive: Boolean,
    onTextChangedListener: (String) -> Unit,
    onSearchClickedListener: () -> Unit,
    onCloseClickedListener: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(modifier = modifier) {
        AnimatedVisibility(
            visible = isSearchActive,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            OutlinedTextField(
                value = text,
                singleLine = true,
                onValueChange = onTextChangedListener,
                shape = RoundedCornerShape(50.dp),
                placeholder = { Text(text = stringResource(R.string.search)) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .padding(end = 32.dp)
            )
        }
        AnimatedVisibility(
            visible = isSearchActive,
            enter = fadeIn(),
            exit = fadeOut(),
            modifier = Modifier.align(Alignment.CenterEnd)
        ) {
            IconButton(onClick = onCloseClickedListener) {
                Icon(imageVector = Icons.Default.Close, contentDescription = stringResource(R.string.close_search))
            }
        }
        AnimatedVisibility(
            visible = !isSearchActive,
            enter = fadeIn(),
            exit = fadeOut(),
            modifier = Modifier.align(Alignment.CenterEnd)
        ) {
            IconButton(onClick = onSearchClickedListener) {
                Icon(imageVector = Icons.Default.Search, contentDescription = stringResource(R.string.open_search))
            }
        }
    }
}

@Preview(name = "Light Mode", showBackground = true)
@Preview(name = "Dark Mode", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun HideableSearchTextFieldPreview() {
    NotesApplicationTheme {
        Surface {
            HideableSearchTextField(
                text = "",
                isSearchActive = true,
                onTextChangedListener = {},
                onSearchClickedListener = {},
                onCloseClickedListener = {}
            )
        }
    }
}
