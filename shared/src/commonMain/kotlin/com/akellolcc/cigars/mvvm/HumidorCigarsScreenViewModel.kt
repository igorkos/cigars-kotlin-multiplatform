package com.akellolcc.cigars.mvvm

import com.akellolcc.cigars.databases.RepositoryType
import com.akellolcc.cigars.databases.extensions.Cigar
import com.akellolcc.cigars.databases.extensions.Humidor
import com.akellolcc.cigars.databases.extensions.HumidorCigar
import com.akellolcc.cigars.databases.repository.CigarHumidorRepository
import dev.icerock.moko.resources.desc.StringDesc


class HumidorCigarsScreenViewModel(val humidor: Humidor) :
    BaseListViewModel<HumidorCigar, HumidorCigarsScreenViewModel.CigarsAction>() {
    override val repository: CigarHumidorRepository =
        database.getRepository(RepositoryType.HumidorCigars, humidor.rowid)

    override fun entitySelected(entity: HumidorCigar) {
        sendEvent(CigarsAction.RouteToCigar(entity.cigar))
    }

    fun humidorDetails() {
        sendEvent(CigarsAction.RouteToHumidorDetails(humidor))
    }

    fun addCigar() {
        sendEvent(CigarsAction.AddCigar())
    }

    fun openHistory() {
        sendEvent(CigarsAction.OpenHistory(1))
    }

    sealed interface CigarsAction {
        data class RouteToHumidorDetails(val humidor: Humidor) : CigarsAction
        data class RouteToCigar(val cigar: Cigar) : CigarsAction
        data class AddCigar(val cigar: Cigar? = null) : CigarsAction
        data class OpenHistory(val dummy: Int) : CigarsAction
        data class ShowError(val error: StringDesc) : CigarsAction
    }
}