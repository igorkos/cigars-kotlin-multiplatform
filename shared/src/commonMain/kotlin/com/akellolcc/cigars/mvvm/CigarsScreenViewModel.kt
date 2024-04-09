package com.akellolcc.cigars.mvvm

import com.akellolcc.cigars.databases.RepositoryType
import com.akellolcc.cigars.databases.extensions.Cigar
import com.akellolcc.cigars.databases.repository.CigarsRepository
import dev.icerock.moko.resources.desc.StringDesc


open class CigarsScreenViewModel : BaseListViewModel<Cigar, CigarsScreenViewModel.CigarsAction>() {
    override val repository: CigarsRepository =  database.getRepository(RepositoryType.Cigars)

    fun cigarSelected(cigar: Cigar) {
        sendEvent(CigarsAction.RouteToCigar(cigar))
    }

    sealed interface CigarsAction {
        data class RouteToCigar(val cigar: Cigar) : CigarsAction
        data class ShowError(val error: StringDesc) : CigarsAction
    }
}