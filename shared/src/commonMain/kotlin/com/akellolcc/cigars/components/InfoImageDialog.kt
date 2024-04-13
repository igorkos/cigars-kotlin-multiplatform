package com.akellolcc.cigars.components

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.akellolcc.cigars.theme.loadImage
import dev.icerock.moko.resources.ImageResource

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun InfoImageDialog(image: ImageResource, onDismissRequest: () -> Unit) {
    Dialog(
        onDismissRequest = onDismissRequest,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(modifier = Modifier.fillMaxSize()) {
            loadImage(image)
            IconButton(
                modifier = Modifier.size(24.dp),
                onClick = { onDismissRequest() }) {
            }
        }
    }

}