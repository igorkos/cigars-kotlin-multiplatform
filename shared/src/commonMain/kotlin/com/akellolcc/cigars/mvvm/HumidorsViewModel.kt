package com.akellolcc.cigars.mvvm

import com.akellolcc.cigars.databases.RepositoryType
import com.akellolcc.cigars.databases.extensions.Humidor
import com.akellolcc.cigars.databases.repository.HumidorsRepository
import dev.icerock.moko.resources.desc.StringDesc


class HumidorsViewModel : BaseListViewModel<Humidor, HumidorsViewModel.Action>() {
    override val repository: HumidorsRepository = database.getRepository(RepositoryType.Humidors)

    override fun entitySelected(entity: Humidor) {
        sendEvent(Action.RouteToHumidor(entity))
    }

    fun addHumidor() {
        sendEvent(Action.AddHumidor(null))
    }

    sealed interface Action {
        data class RouteToHumidor(val humidor: Humidor) : Action
        data class AddHumidor(val dummy: Any?) : Action
        data class ShowError(val error: StringDesc) : Action
    }
}