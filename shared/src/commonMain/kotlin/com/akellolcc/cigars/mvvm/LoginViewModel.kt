/*
 * Copyright (C) 2024 Igor Kosulin
 * Last modified 1/13/24, 6:35 PM
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

package com.akellolcc.cigars.mvvm

import dev.icerock.moko.mvvm.flow.CFlow
import dev.icerock.moko.mvvm.flow.CMutableStateFlow
import dev.icerock.moko.mvvm.flow.CStateFlow
import dev.icerock.moko.mvvm.flow.cFlow
import dev.icerock.moko.mvvm.flow.cMutableStateFlow
import dev.icerock.moko.mvvm.flow.cStateFlow
import dev.icerock.moko.mvvm.viewmodel.ViewModel
import dev.icerock.moko.resources.desc.StringDesc
import dev.icerock.moko.resources.desc.desc
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch


class LoginViewModel : ViewModel() {

    val login: CMutableStateFlow<String> = MutableStateFlow("igor@kosulin.com").cMutableStateFlow()
    val password: CMutableStateFlow<String> = MutableStateFlow("test").cMutableStateFlow()

    private val _isLoading: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val isLoading: CStateFlow<Boolean> = _isLoading.cStateFlow()

    private val _isLogin: MutableStateFlow<Boolean> = MutableStateFlow(true)
    val isLogin: CStateFlow<Boolean> = _isLoading.cStateFlow()


    val isLoginButtonEnabled: CStateFlow<Boolean> =
        combine(isLoading, login, password) { isLoading, login, password ->
            isLoading.not() && login.isNotBlank() && password.isNotBlank()
        }.stateIn(viewModelScope, SharingStarted.Lazily, false).cStateFlow()

    private val _actions = Channel<Action>(Channel.BUFFERED)
    val actions: CFlow<Action> get() = _actions.receiveAsFlow().cFlow()

    fun onLoginPressed() {
        _isLoading.value = true
        viewModelScope.launch {
            _isLoading.value = false
            if (login.value != "error") {
                _actions.send(Action.RouteToSuccess)
            } else {
                _actions.send(Action.ShowError("some error!".desc()))
            }
        }
    }

    sealed interface Action {
        object RouteToSuccess : Action
        data class ShowError(val error: StringDesc) : Action
    }
}