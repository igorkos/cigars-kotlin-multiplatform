/*
 * Copyright (C) 2024 Igor Kosulin
 * Last modified 4/22/24, 8:42 PM
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.akellolcc.cigars.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.registry.rememberScreen
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.akellolcc.cigars.mvvm.LoginViewModel
import com.akellolcc.cigars.screens.components.DefaultButton
import com.akellolcc.cigars.screens.navigation.SharedScreen
import com.akellolcc.cigars.theme.Localize
import com.akellolcc.cigars.theme.LoginBackground
import com.akellolcc.cigars.utils.ui.BackHandler
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

        // val isLoading: Boolean by viewModel!!.isLoading.collectAsState()
        // val isEnabled: Boolean by viewModel!!.isLoginButtonEnabled.collectAsState()
        //  val isLogin: Boolean by viewModel!!.isLoginButtonEnabled.collectAsState()

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
