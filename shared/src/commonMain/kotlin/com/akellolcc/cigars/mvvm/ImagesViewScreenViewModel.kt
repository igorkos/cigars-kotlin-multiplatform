package com.akellolcc.cigars.mvvm

import com.akellolcc.cigars.camera.SharedImage
import com.akellolcc.cigars.databases.RepositoryType
import com.akellolcc.cigars.databases.extensions.Cigar
import com.akellolcc.cigars.databases.extensions.CigarImage
import com.akellolcc.cigars.databases.extensions.Humidor
import com.akellolcc.cigars.databases.repository.ImagesRepository
import dev.icerock.moko.resources.desc.StringDesc

abstract class BaseImagesViewScreenViewModel(val id: Long) :
    BaseListViewModel<CigarImage, BaseImagesViewScreenViewModel.Action>() {
    fun addImage(image: SharedImage) {
        image.toByteArray()?.let {
            repository.add(CigarImage(-1, bytes = it), null)
        }
    }

    override fun entitySelected(entity: CigarImage) {}

    sealed interface Action {
        data class ShowError(val error: StringDesc) : Action
    }
}

class CigarImagesViewScreenViewModel(val cigar: Cigar) :
    BaseImagesViewScreenViewModel(cigar.rowid) {
    override val repository: ImagesRepository = database.getRepository(RepositoryType.CigarImages,cigar.rowid)
}


class HumidorImagesViewScreenViewModel(val humidor: Humidor) : BaseImagesViewScreenViewModel(humidor.rowid) {
    override val repository: ImagesRepository = database.getRepository(RepositoryType.HumidorImages,humidor.rowid)
}