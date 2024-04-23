/*
 * Copyright (C) 2024 Igor Kosulin
 * Last modified 4/23/24, 1:07 PM
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

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.akellolcc.cigars.camera.PermissionCallback
import com.akellolcc.cigars.camera.PermissionStatus
import com.akellolcc.cigars.camera.PermissionType
import com.akellolcc.cigars.camera.SharedImage
import com.akellolcc.cigars.databases.RepositoryType
import com.akellolcc.cigars.databases.extensions.Cigar
import com.akellolcc.cigars.databases.extensions.CigarImage
import com.akellolcc.cigars.databases.extensions.Humidor
import com.akellolcc.cigars.databases.repository.ImagesRepository
import com.akellolcc.cigars.logging.Log
import com.badoo.reaktive.observable.flatMap
import com.badoo.reaktive.observable.flatMapIterable
import com.badoo.reaktive.observable.observableOf
import com.badoo.reaktive.observable.subscribe
import com.badoo.reaktive.observable.take
import dev.icerock.moko.resources.desc.StringDesc

abstract class BaseImagesViewScreenViewModel(val id: Long, select: Int) :
    BaseListViewModel<CigarImage, BaseImagesViewScreenViewModel.Action>(), PermissionCallback {
    var launchCamera by mutableStateOf(false)
    var launchGallery by mutableStateOf(false)
    var lastSelect by mutableStateOf(select)
    var select by mutableStateOf(select)

    var permissionCamera by mutableStateOf(false)
    var permissionGallery by mutableStateOf(false)
    val isPermissionGranted: Boolean
        get() = permissionCamera && permissionGallery

    abstract fun getImage(bytes: ByteArray): CigarImage

    fun addImages(images: List<SharedImage>) {
        lastSelect = entities.size
        observableOf(images).take(1).flatMapIterable {
            it
        }.flatMap {
            observableOf(it.toByteArray())
        }.flatMap {
            if (it != null) {
                repository.add(getImage(it))
            } else {
                observableOf(null)
            }
        }.subscribe(
            onNext = {
                Log.debug("Image added")
            },
            onError = {
                Log.error("Error add image $it")
            },
            onComplete = {
                Log.debug("Complete add images")
                loading = false
            }
        )
    }

    override fun entitySelected(entity: CigarImage) {}

    override fun onPermissionStatus(permissionType: PermissionType, status: PermissionStatus) {
        Log.debug("Permission $permissionType -> $status")
        when (status) {
            PermissionStatus.SHOW_RATIONAL -> {}
            PermissionStatus.DENIED -> {
                if (permissionType == PermissionType.GALLERY) {
                    permissionGallery = false
                } else {
                    permissionCamera = false
                }
            }

            PermissionStatus.GRANTED -> {
                if (permissionType == PermissionType.GALLERY) {
                    permissionGallery = true
                } else {
                    permissionCamera = true
                }
            }
        }
    }

    sealed interface Action {
        data class ShowError(val error: StringDesc) : Action
    }
}

class CigarImagesViewScreenViewModel(val cigar: Cigar, select: Int) :
    BaseImagesViewScreenViewModel(cigar.rowid, select) {
    override fun getImage(bytes: ByteArray): CigarImage {
        return CigarImage(-1, bytes = bytes, cigarId = cigar.rowid)
    }

    override val repository: ImagesRepository =
        database.getRepository(RepositoryType.CigarImages, cigar.rowid)

    companion object Factory : ViewModelsFactory<CigarImagesViewScreenViewModel>() {
        override fun factory(data: Any?): CigarImagesViewScreenViewModel {
            val params = data as Pair<*, *>
            return CigarImagesViewScreenViewModel(params.first as Cigar, params.second as Int)
        }
    }

}


class HumidorImagesViewScreenViewModel(val humidor: Humidor, select: Int) :
    BaseImagesViewScreenViewModel(humidor.rowid, select) {
    override val repository: ImagesRepository =
        database.getRepository(RepositoryType.HumidorImages, humidor.rowid)

    override fun getImage(bytes: ByteArray): CigarImage {
        return CigarImage(-1, bytes = bytes, humidorId = humidor.rowid)
    }

    companion object Factory : ViewModelsFactory<HumidorImagesViewScreenViewModel>() {
        override fun factory(data: Any?): HumidorImagesViewScreenViewModel {
            val params = data as Pair<*, *>
            return HumidorImagesViewScreenViewModel(params.first as Humidor, params.second as Int)
        }
    }
}