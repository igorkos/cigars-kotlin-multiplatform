/*******************************************************************************************************************************************
 * Copyright (C) 2024 Igor Kosulin
 * Last modified 5/31/24, 11:25 AM
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

package com.akellolcc.cigars.mvvm.humidor

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.paging.PagingData
import com.akellolcc.cigars.databases.createRepository
import com.akellolcc.cigars.databases.models.CigarImage
import com.akellolcc.cigars.databases.models.Humidor
import com.akellolcc.cigars.databases.models.HumidorCigar
import com.akellolcc.cigars.databases.repository.HumidorImagesRepository
import com.akellolcc.cigars.databases.repository.HumidorsRepository
import com.akellolcc.cigars.mvvm.base.DatabaseViewModel
import com.akellolcc.cigars.utils.ObjectFactory
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow

class HumidorDetailsScreenViewModel(private var humidor: Humidor) :
    DatabaseViewModel<Humidor>() {
    private var imagesDatabase: HumidorImagesRepository? = null
    private var humidorsDatabase: HumidorsRepository? = createRepository(HumidorsRepository::class)
    private var disposable: List<Job> = mutableListOf()

    var editing by mutableStateOf(humidor.rowid < 0)
    var name by mutableStateOf(humidor.name)
    var brand by mutableStateOf(humidor.brand)
    var holds by mutableStateOf(humidor.holds)
    var count by mutableStateOf(humidor.count)
    var temperature by mutableStateOf(humidor.temperature)
    var humidity by mutableStateOf(humidor.humidity)
    var notes by mutableStateOf(humidor.notes)
    var link by mutableStateOf(humidor.link)
    var type by mutableStateOf(humidor.type)

    var images by mutableStateOf<Flow<PagingData<CigarImage>>?>(null)
    var humidors by mutableStateOf(listOf<HumidorCigar>())


    fun observeHumidor() {
        imagesDatabase = createRepository(HumidorImagesRepository::class, humidor.rowid)
        disposable += execute(humidorsDatabase!!.observe(humidor.rowid)) {
            if (!editing) {
                name = it.name
                brand = it.brand
                holds = it.holds
                count = it.count
                temperature = it.temperature
                humidity = it.humidity
                notes = it.notes
                link = it.link
                type = it.type
            }
        }

        images = imagesDatabase!!.paging(null, null)
    }

    fun verifyFields(): Boolean {
        return name.isNotBlank() && brand.isNotBlank() && holds > 0
    }

    fun saveEdit() {
        val updated = Humidor(
            humidor.rowid,
            name = name,
            brand = brand,
            holds = holds,
            count = count,
            temperature = temperature,
            humidity = humidity,
            notes = notes,
            link = link
        )
        if (humidor.rowid < 0) {
            /*  humidorsDatabase?.add(updated) {
                  humidor = humidorsDatabase?.get(it)!!
                  observeHumidor()
              }*/

        } else {
            humidorsDatabase?.update(updated)
        }

        editing = false
    }

    fun cancelEdit() {
        editing = false
        if (humidorsDatabase == null) {
            onBackPress()
        }
    }

    override fun onDispose() {
        super.onDispose()
        if (disposable.isNotEmpty()) {
            disposable.forEach {
                it.cancel()
            }
            disposable = mutableListOf()
        }
    }

    companion object Factory : ObjectFactory<HumidorDetailsScreenViewModel>() {
        override fun factory(data: Any?): HumidorDetailsScreenViewModel {
            return HumidorDetailsScreenViewModel(data as Humidor)
        }
    }

    sealed interface HumidorDetailsAction : CommonAction
}
