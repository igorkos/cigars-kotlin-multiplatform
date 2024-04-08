package com.akellolcc.cigars.mvvm

import com.akellolcc.cigars.databases.RepositoryType
import com.akellolcc.cigars.databases.extensions.Cigar
import com.akellolcc.cigars.databases.repository.FavoriteCigarsRepository
import dev.icerock.moko.resources.desc.StringDesc


class FavoritesScreenViewModel : BaseListViewModel<Cigar, FavoritesScreenViewModel.CigarsAction>() {
    override val repository: FavoriteCigarsRepository = database.getRepository(RepositoryType.Favorites)

    fun cigarSelected(cigar: Cigar) {
        sendEvent(CigarsAction.RouteToCigar(cigar))
    }

    sealed interface CigarsAction {
        data class RouteToCigar(val cigar: Cigar) : CigarsAction
        data class ShowError(val error: StringDesc) : CigarsAction
    }
}