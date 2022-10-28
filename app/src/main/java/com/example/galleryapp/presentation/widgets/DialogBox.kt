package com.example.galleryapp.presentation.widgets

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.galleryapp.ui.theme.bigFontSize
import com.example.galleryapp.ui.theme.defaultFontSize
import com.example.galleryapp.ui.theme.smallPadding

@Composable
fun DialogBox(
    title: String,
    description: String,
    acceptString: String,
    deniedString: String,
    onSettingsClick: () -> Unit,
    onDismiss: () -> Unit
) {
    Dialog(
        onDismissRequest = {
            onDismiss()
        }
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(8.dp)),
            elevation = 4.dp
        ) {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .background(color = MaterialTheme.colors.secondary),
                    contentAlignment = Alignment.Center
                ) {

                }

                Text(
                    modifier = Modifier.padding(top = 16.dp, bottom = 16.dp),
                    text = title,
                    textAlign = TextAlign.Center,
                    style = TextStyle(
                        fontWeight = FontWeight.Medium,
                        fontSize = bigFontSize
                    )
                )
                Text(
                    modifier = Modifier.padding(horizontal = smallPadding),
                    text = description,
                    textAlign = TextAlign.Center,
                    style = TextStyle(
                        fontWeight = FontWeight.Normal,
                        fontSize = defaultFontSize
                    )
                )

                Button(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 36.dp, start = 36.dp, end = 36.dp, bottom = 8.dp),
                    onClick = {
                        onSettingsClick()
                        onDismiss()
                    }
                ) {
                    Text(
                        text = acceptString,
                        color = MaterialTheme.colors.onPrimary,
                        style = TextStyle(
                            fontWeight = FontWeight.Medium,
                            fontSize = defaultFontSize
                        )
                    )
                }

                TextButton(
                    onClick = {
                        onDismiss()
                    }) {
                    Text(
                        text = deniedString,
                        color = MaterialTheme.colors.onBackground,
                        style = TextStyle(
                            fontWeight = FontWeight.Normal,
                            fontSize = 14.sp
                        )
                    )
                }
            }
        }
    }
}