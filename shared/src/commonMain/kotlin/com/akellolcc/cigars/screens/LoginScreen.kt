package com.akellolcc.cigars.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.registry.rememberScreen
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.akellolcc.cigars.components.DefaultButton
import com.akellolcc.cigars.mvvm.LoginViewModel
import com.akellolcc.cigars.navigation.SharedScreen
import com.akellolcc.cigars.theme.Localize
import com.akellolcc.cigars.theme.LoginBackground
import com.akellolcc.cigars.ui.BackHandler
import dev.icerock.moko.mvvm.compose.getViewModel
import dev.icerock.moko.mvvm.compose.viewModelFactory
import dev.icerock.moko.mvvm.flow.compose.observeAsActions

class LoginScreen : Screen {

    private var viewModel: LoginViewModel? = null

    @Composable
    override fun Content() {
        if (viewModel == null) {
            viewModel = getViewModel(
                key = "Login",
                factory = viewModelFactory { LoginViewModel() })
        }

        BackHandler {}
        val navigator = LocalNavigator.currentOrThrow
        val postScreen = rememberScreen(SharedScreen.MainScreen)

        viewModel?.actions?.observeAsActions {
            when (it) {
                LoginViewModel.Action.RouteToSuccess -> {
                    navigator.push(postScreen)
                }

                is LoginViewModel.Action.ShowError -> TODO()
            }
        }

        val isLoading: Boolean by viewModel!!.isLoading.collectAsState()
        val isEnabled: Boolean by viewModel!!.isLoginButtonEnabled.collectAsState()
        val isLogin: Boolean by viewModel!!.isLoginButtonEnabled.collectAsState()

        LoginBackground {
            Box(
                modifier = with(Modifier) {
                    fillMaxSize().statusBarsPadding().navigationBarsPadding()
                        .padding(bottom = 20.dp)
                }
            ) {
                DefaultButton(
                    title = Localize.title_login,
                    enabled = true,
                    modifier = with(Modifier) {
                        fillMaxWidth(0.7f).align(alignment = Alignment.BottomCenter)
                    },
                    onClick = { viewModel!!.onLoginPressed() })
                /*{
                    if (!isLoading) {
                        Text(text = Localize.title_login)
                    } else {
                        rotateImage(Images.loading_spinner, Size(20f, 20f))
                    }
                }

                 */
            }
        }
    }
}
