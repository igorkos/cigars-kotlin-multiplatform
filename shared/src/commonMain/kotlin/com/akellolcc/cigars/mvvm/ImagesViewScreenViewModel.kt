/*
 * Copyright (C) 2024 Igor Kosulin
 * Last modified 4/15/24, 10:04 PM
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
            repository.add(CigarImage(-1, bytes = it))
        }
    }

    override fun entitySelected(entity: CigarImage) {}

    sealed interface Action {
        data class ShowError(val error: StringDesc) : Action
    }
}

class CigarImagesViewScreenViewModel(val cigar: Cigar) :
    BaseImagesViewScreenViewModel(cigar.rowid) {
    override val repository: ImagesRepository =
        database.getRepository(RepositoryType.CigarImages, cigar.rowid)
}


class HumidorImagesViewScreenViewModel(val humidor: Humidor) :
    BaseImagesViewScreenViewModel(humidor.rowid) {
    override val repository: ImagesRepository =
        database.getRepository(RepositoryType.HumidorImages, humidor.rowid)
}