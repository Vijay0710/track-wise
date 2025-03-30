package com.eyeshield.sharewise.client

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun ClientScreen(
    message: String,
    uiAction: (ClientScreenViewModel.UiAction) -> Unit
) {

    var textFieldmessage by rememberSaveable {
        mutableStateOf("")
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.primaryContainer
    ) {
        Column(
            modifier = Modifier
                .padding(it)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = "Client Screen",
                style = MaterialTheme.typography.headlineSmall,
                textAlign = TextAlign.Center,
            )

            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 20.dp),
                text = "Received Message: $message",
                style = MaterialTheme.typography.bodySmall,
                textAlign = TextAlign.Center
            )

            TextField(
                modifier = Modifier.fillMaxWidth(),
                value = textFieldmessage,
                onValueChange = {
                    textFieldmessage = it
                }
            )

            Button(
                modifier = Modifier.padding(top = 24.dp),
                onClick = {
                    uiAction(ClientScreenViewModel.UiAction.SendMessage(textFieldmessage))
                },
                content = {
                    Text(text = "Send Message")
                }
            )
        }
    }
}

@Preview
@Composable
fun PreviewClientScreen() {
    ClientScreen(
        message = "Hello",
        uiAction = {

        }
    )
}