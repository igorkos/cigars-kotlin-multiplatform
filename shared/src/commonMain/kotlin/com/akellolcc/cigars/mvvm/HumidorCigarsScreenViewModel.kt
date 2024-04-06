package com.akellolcc.cigars.mvvm

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import com.akellolcc.cigars.databases.extensions.Cigar
import com.akellolcc.cigars.databases.extensions.Humidor
import com.akellolcc.cigars.databases.extensions.HumidorCigar
import com.akellolcc.cigars.databases.repository.impl.SqlDelightHumidorCigarsRepository
import dev.icerock.moko.resources.desc.StringDesc


class HumidorCigarsScreenViewModel(val humidor: Humidor) :
    BaseListViewModel<HumidorCigar, HumidorCigarsScreenViewModel.CigarsAction>() {
    override val database: SqlDelightHumidorCigarsRepository =
        SqlDelightHumidorCigarsRepository(humidor.rowid)

    fun cigarSelected(cigar: Cigar) {
        sendEvent(CigarsAction.RouteToCigar(cigar))
    }

    fun humidorDetails() {
        sendEvent(CigarsAction.RouteToHumidorDetails(humidor))
    }

    fun addCigar() {
        sendEvent(CigarsAction.AddCigar())
    }

    @Composable
    override fun asState(): State<List<HumidorCigar>> {
        return database.observeAll().collectAsState(listOf())
    }

    sealed interface CigarsAction {
        data class RouteToHumidorDetails(val humidor: Humidor) : CigarsAction
        data class RouteToCigar(val cigar: Cigar) : CigarsAction
        data class AddCigar(val cigar: Cigar? = null) : CigarsAction
        data class ShowError(val error: StringDesc) : CigarsAction
    }
}