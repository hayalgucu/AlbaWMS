package com.hayalgucu.albawms.customviews

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun TableCell(
    text: String,
    width: Dp = 80.dp,
    padding: Int = 8,
    textAlign: TextAlign = TextAlign.Center
) {
    Text(
        text = text,
        Modifier
            .border(0.5.dp, Color.Black)
            .width(width)
            .padding(padding.dp),
        overflow = TextOverflow.Visible,
        maxLines = 1, textAlign = textAlign
    )
}

@Composable
fun TableHeaderCell(
    text: String,
    width: Dp = 80.dp,
    padding: Int = 8,
    onClick: () -> Unit = {}
) {
    Text(
        text = text,
        Modifier
            .border(0.5.dp, Color.Black)
            .clickable {
                onClick()
            }
            .width(width)
            .padding(padding.dp),
        overflow = TextOverflow.Visible,
        maxLines = 1,
        textAlign = TextAlign.Center,
        color = Color.Black
    )
}