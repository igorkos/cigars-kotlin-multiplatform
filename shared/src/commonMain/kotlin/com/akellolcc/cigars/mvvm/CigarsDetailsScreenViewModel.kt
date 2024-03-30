package com.akellolcc.cigars.mvvm

import com.akellolcc.cigars.databases.extensions.Cigar
import com.akellolcc.cigars.databases.extensions.CigarImage
import dev.icerock.moko.mvvm.flow.CFlow
import dev.icerock.moko.mvvm.flow.CMutableStateFlow
import dev.icerock.moko.mvvm.flow.cFlow
import dev.icerock.moko.mvvm.flow.cMutableStateFlow
import dev.icerock.moko.mvvm.viewmodel.ViewModel
import dev.icerock.moko.resources.desc.StringDesc
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch


class CigarsDetailsScreenViewModel(val cigar: Cigar) : ViewModel() {

    private val _images: CMutableStateFlow<List<CigarImage>> = MutableStateFlow(listOf<CigarImage>()).cMutableStateFlow()
    val images: StateFlow<List<CigarImage>> = _images

    private val _actions = Channel<Action>(Channel.BUFFERED)
    val actions: CFlow<Action> get() = _actions.receiveAsFlow().cFlow()

    fun fetchImages() {
        _images.value = cigar.images
    }

    fun cigarSelected( cigar: Cigar) {
        viewModelScope.launch {
            _actions.send(Action.RouteToCigar(cigar))
        }
    }

    sealed interface Action {
        data class RouteToCigar(val cigar: Cigar) : Action
        data class ShowError(val error: StringDesc) : Action
    }
}