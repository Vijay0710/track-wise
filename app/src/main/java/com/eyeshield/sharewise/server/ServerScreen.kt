package com.eyeshield.sharewise.server

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import java.net.Socket

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ServerScreen(
    uiState: ServerViewModel.UiState = ServerViewModel.UiState()
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.primaryContainer
    ) {
        Column(
            modifier = Modifier
                .padding(it)
                .padding(24.dp)
        ) {
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = "Server Screen",
                style = MaterialTheme.typography.headlineSmall,
                textAlign = TextAlign.Center,
            )

            Text(
                text = uiState.message,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                textAlign = TextAlign.Center
            )

            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp)
            ) {

                stickyHeader {
                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 20.dp),
                        text = "Connected Clients",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.W600,
                        textAlign = TextAlign.Center
                    )
                }

                uiState.clients.values.forEach { client ->
                    item(client) {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(24.dp)
                            ) {
                                Text(
                                    text = client,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSecondary
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
@Preview
private fun PreviewServerScreen() {
    ServerScreen(
        uiState = ServerViewModel.UiState(
            message = "This is a demo message from Client",
            clients = mapOf(
                Socket() to "192.168.0.1",
            )
        )
    )
}