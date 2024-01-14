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