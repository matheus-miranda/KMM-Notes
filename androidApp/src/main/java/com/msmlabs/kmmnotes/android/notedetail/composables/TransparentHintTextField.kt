package com.msmlabs.kmmnotes.android.notedetail.composables

import android.content.res.Configuration
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusState
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import com.msmlabs.kmmnotes.android.NotesApplicationTheme

@Composable
fun TransparentHintTextField(
    text: String,
    hint: String,
    isHintVisible: Boolean,
    onValueChangedListener: (String) -> Unit,
    modifier: Modifier = Modifier,
    textStyle: TextStyle = TextStyle(),
    singleLine: Boolean = false,
    onFocusChangedListener: (FocusState) -> Unit,
) {
    Box(modifier = modifier) {
        BasicTextField(
            value = text,
            onValueChange = onValueChangedListener,
            singleLine = singleLine,
            textStyle = textStyle,
            modifier = Modifier
                .fillMaxWidth()
                .onFocusChanged { state ->
                    onFocusChangedListener(state)
                }
        )
        if (isHintVisible) {
            Text(
                text = hint,
                style = textStyle,
                color = Color.DarkGray
            )
        }
    }
}

@Preview(name = "Light Mode", showBackground = true)
@Preview(name = "Dark Mode", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun TransparentHintTextFieldPreview() {
    NotesApplicationTheme {
        Surface {
            TransparentHintTextField(
                text = "",
                hint = "Hint",
                isHintVisible = true,
                onValueChangedListener = {},
                onFocusChangedListener = {}
            )
        }
    }
}
