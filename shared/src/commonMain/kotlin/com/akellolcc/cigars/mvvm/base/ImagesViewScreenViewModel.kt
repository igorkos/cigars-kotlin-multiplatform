/*******************************************************************************************************************************************
 * Copyright (C) 2024 Igor Kosulin
 * Last modified 5/31/24, 6:06 PM
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
 ******************************************************************************************************************************************/

package com.akellolcc.cigars.mvvm.base

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.akellolcc.cigars.camera.PermissionCallback
import com.akellolcc.cigars.camera.PermissionStatus
import com.akellolcc.cigars.camera.PermissionType
import com.akellolcc.cigars.camera.SharedImage
import com.akellolcc.cigars.databases.createRepository
import com.akellolcc.cigars.databases.models.Cigar
import com.akellolcc.cigars.databases.models.CigarImage
import com.akellolcc.cigars.databases.models.Humidor
import com.akellolcc.cigars.databases.repository.CigarImagesRepository
import com.akellolcc.cigars.databases.repository.HumidorImagesRepository
import com.akellolcc.cigars.logging.Log
import com.akellolcc.cigars.utils.ObjectFactory

abstract class BaseImagesViewScreenViewModel(val id: Long, select: Int) : BaseListViewModel<CigarImage>(), PermissionCallback {
    var launchCamera by mutableStateOf(false)
    var launchGallery by mutableStateOf(false)
    var select by mutableStateOf(select)

    var permissionCamera by mutableStateOf(false)
    var permissionGallery by mutableStateOf(false)
    val isPermissionGranted: Boolean
        get() = permissionCamera && permissionGallery

    abstract fun getImage(bytes: ByteArray): CigarImage

    fun addImages(images: List<SharedImage>) {
        val list = images.map { getImage(it.toByteArray()!!) }
        executeQuery(repository.addAll(list)) {
            select = repository.count().toInt() - 1
        }
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

    sealed interface ImageScreenAction : CommonAction {
        data class AddImages(val images: List<SharedImage>) : ImageScreenAction
    }
}

class CigarImagesViewScreenViewModel(val cigar: Cigar, select: Int) :
    BaseImagesViewScreenViewModel(cigar.rowid, select) {
    override fun getImage(bytes: ByteArray): CigarImage {
        return CigarImage(-1, bytes = bytes, cigarId = cigar.rowid)
    }

    override val repository: CigarImagesRepository =
        createRepository(CigarImagesRepository::class, cigar.rowid)

    companion object Factory : ObjectFactory<CigarImagesViewScreenViewModel>() {
        override fun factory(data: Any?): CigarImagesViewScreenViewModel {
            val params = data as Pair<*, *>
            return CigarImagesViewScreenViewModel(params.first as Cigar, params.second as Int)
        }
    }

}


class HumidorImagesViewScreenViewModel(val humidor: Humidor, select: Int) :
    BaseImagesViewScreenViewModel(humidor.rowid, select) {
    override val repository: HumidorImagesRepository =
        createRepository(HumidorImagesRepository::class, humidor.rowid)

    override fun getImage(bytes: ByteArray): CigarImage {
        return CigarImage(-1, bytes = bytes, humidorId = humidor.rowid)
    }

    companion object Factory : ObjectFactory<HumidorImagesViewScreenViewModel>() {
        override fun factory(data: Any?): HumidorImagesViewScreenViewModel {
            val params = data as Pair<*, *>
            return HumidorImagesViewScreenViewModel(params.first as Humidor, params.second as Int)
        }
    }
}