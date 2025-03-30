package com.eyeshield.sharewise

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.eyeshield.sharewise.client.ClientScreen
import com.eyeshield.sharewise.client.ClientScreenViewModel
import com.eyeshield.sharewise.server.ServerScreen
import com.eyeshield.sharewise.server.ServerViewModel
import com.eyeshield.sharewise.ui.theme.TrackWiseTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TrackWiseTheme {
                val navController = rememberNavController()
                /**
                 * Make sure to change the start destination to server or client.
                 * For Testing Change the start destination to server and run in an emulator
                 * and Change the start destination to client and run in another emulator followed by server
                 * **/
                NavHost(
                    navController = navController,
                    startDestination = "client",
                ) {
                    composable("server") {
                        val viewModel = hiltViewModel<ServerViewModel>()
                        val uiState = viewModel.uiState.collectAsStateWithLifecycle().value

                        ServerScreen(
                            uiState = uiState,
                        )
                    }

                    composable("client") {
                        val viewModel = hiltViewModel<ClientScreenViewModel>()
                        val uiState = viewModel.messages.collectAsStateWithLifecycle()

                        ClientScreen(
                            message = uiState.value.message,
                            uiAction = viewModel::onUiAction
                        )
                    }
                }
            }
        }
    }
}
