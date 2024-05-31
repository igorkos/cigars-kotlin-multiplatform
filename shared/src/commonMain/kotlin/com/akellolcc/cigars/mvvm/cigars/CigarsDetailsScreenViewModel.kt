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

package com.akellolcc.cigars.mvvm.cigars

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.paging.PagingData
import com.akellolcc.cigars.databases.createRepository
import com.akellolcc.cigars.databases.models.Cigar
import com.akellolcc.cigars.databases.models.CigarImage
import com.akellolcc.cigars.databases.models.Humidor
import com.akellolcc.cigars.databases.models.HumidorCigar
import com.akellolcc.cigars.databases.repository.CigarHistoryRepository
import com.akellolcc.cigars.databases.repository.CigarHumidorRepository
import com.akellolcc.cigars.databases.repository.CigarHumidorsRepository
import com.akellolcc.cigars.databases.repository.CigarImagesRepository
import com.akellolcc.cigars.databases.repository.CigarsRepository
import com.akellolcc.cigars.databases.repository.HumidorsRepository
import com.akellolcc.cigars.mvvm.base.DatabaseViewModel
import com.akellolcc.cigars.screens.components.ValuePickerItem
import com.akellolcc.cigars.theme.Images
import com.akellolcc.cigars.utils.ObjectFactory
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow


class CigarsDetailsScreenViewModel(private val cigar: Cigar) : DatabaseViewModel<Cigar>() {
    private var cigarsRepository: CigarsRepository = createRepository(CigarsRepository::class)
    private var humidorsRepository: HumidorsRepository = createRepository(HumidorsRepository::class)
    private var imagesRepository: CigarImagesRepository? = null
    private var cigarHumidorRepository: CigarHumidorRepository? = null
    private var cigarHistoryRepository: CigarHistoryRepository? = null
    private var disposable: List<Job> = mutableListOf()

    var editing by mutableStateOf(false)//cigar.rowid < 0)
    var name by mutableStateOf(cigar.name)
    var brand by mutableStateOf(cigar.brand)
    var country by mutableStateOf(cigar.country)
    var shape by mutableStateOf(cigar.cigar)
    var length by mutableStateOf(cigar.length)
    var gauge by mutableStateOf(cigar.gauge)
    var wrapper by mutableStateOf(cigar.wrapper)
    var binder by mutableStateOf(cigar.binder)
    var filler by mutableStateOf(cigar.filler)
    var strength by mutableStateOf(cigar.strength)
    var rating by mutableStateOf(cigar.rating)
    var myRating by mutableStateOf(cigar.myrating)
    var favorites by mutableStateOf(cigar.favorites)
    var notes by mutableStateOf(cigar.notes)
    var link by mutableStateOf(cigar.link)
    var count by mutableStateOf(cigar.count)

    var images by mutableStateOf<Flow<PagingData<CigarImage>>?>(null)
    var humidors by mutableStateOf(listOf<HumidorCigar>())


    var humidorCigarsCount by mutableStateOf<HumidorCigar?>(null)
    var infoDialog by mutableStateOf(InfoActions.None)
    var cigarRating by mutableStateOf(false)
    var moveCigarDialog by mutableStateOf(false)

    /**
     * Observing cigar state
     */
    fun observeCigar() {
        if (cigar.rowid >= 0) {
            imagesRepository = createRepository(CigarImagesRepository::class, cigar.rowid)
            cigarHumidorRepository = createRepository(CigarHumidorsRepository::class, cigar.rowid)
            cigarHistoryRepository = createRepository(CigarHistoryRepository::class, cigar.rowid)
            loading = true
            disposable += execute(cigarsRepository.observe(cigar.rowid)) {
                if (!editing) {
                    name = it.name
                    brand = it.brand
                    country = it.country
                    shape = it.cigar
                    length = it.length
                    gauge = it.gauge
                    wrapper = it.wrapper
                    binder = it.binder
                    filler = it.filler
                    strength = it.strength
                    rating = it.rating
                    myRating = it.myrating
                    favorites = it.favorites
                    notes = it.notes
                    link = it.link
                    count = it.count
                }
            }

            images = imagesRepository!!.paging(null, null)

            disposable += execute(cigarHumidorRepository!!.all()) {
                humidors = it
            }
        } else {
            loading = false
        }
    }

    /**
     * Editing cigar
     */
    fun verifyFields(): Boolean {
        return name.isNotBlank()
                && brand?.isNotBlank() == true
                && country?.isNotBlank() == true
                && length.isNotBlank() && gauge != 0L
                && wrapper.isNotBlank()
                && binder.isNotBlank()
                && filler.isNotBlank()
    }

    fun onSaveEdit() {
        val updated = Cigar(
            cigar.rowid,
            name = name,
            brand = brand,
            country = country,
            date = cigar.date,
            cigar = shape,
            wrapper = wrapper,
            binder = binder,
            gauge = gauge,
            length = length,
            strength = strength,
            rating = rating,
            myrating = myRating,
            notes = notes,
            filler = filler,
            link = link,
            count = count,
            shopping = cigar.shopping,
            favorites = favorites,
            price = cigar.price,
        )
        executeQuery(cigarsRepository.update(updated)) {
            editing = false
        }
    }

    fun addCigar(humidor: Humidor, count: Long, price: Double? = null) {
        cigar.count = count
        cigar.price = price
        executeQuery(cigarsRepository.add(cigar, humidor)) {
            editing = false
            moveCigarDialog = false
        }
    }

    fun onCancelEdit() {
        if (disposable.isNotEmpty()) {
            disposable.forEach {
                it.cancel()
            }
            disposable = mutableListOf()
            observeCigar()
        } else {
            onBackPress()
        }
        editing = false
    }

    /**
     *  Updating cigar no edit mode
     */
    fun favorite() {
        if (disposable.isNotEmpty()) {
            executeQuery(
                cigarsRepository.updateFavorite(!favorites, cigar)
            )
        }
    }

    fun updateCigarCount(entity: HumidorCigar, count: Long, price: Double? = null) {
        cigarHumidorRepository?.let {
            if (entity.count != count) {
                executeQuery(it.updateCount(entity, count, price)) { humidorCigarsCount = null }
            }
        }
    }

    fun updateCigarRating(rating: Long) {
        executeQuery(
            cigarsRepository.updateRating(rating, cigar)
        )
    }

    /**
     * Moving cigars cigar to humidor
     */
    fun humidorsCount(): Long {
        val repo = createRepository(HumidorsRepository::class)
        return repo.count()
    }

    fun moveFromHumidors(): List<ValuePickerItem<HumidorCigar>> {
        return humidors.map {
            ValuePickerItem(it, it.humidor.name, Images.tab_icon_humidors)
        }
    }

    fun moveToHumidors(from: Humidor?): List<ValuePickerItem<Humidor>> {
        val allHumidors = humidorsRepository.allSync()
        val to = if (from == null) allHumidors else allHumidors - from
        return to.map { ValuePickerItem(it, it.name, Images.tab_icon_humidors) }
    }

    fun moveCigar(from: HumidorCigar, to: Humidor, count: Long) {
        cigarHumidorRepository?.let {
            executeQuery(it.moveCigar(from, to, count)) { moveCigarDialog = false }
        }
    }

    /**
     * Events
     */
    fun addToHumidor(humidor: HumidorCigar) {
        sendEvent(CigarsDetailsAction.AddToHumidor(humidor))
    }

    fun openHumidor(humidor: Humidor) {
        sendEvent(CigarsDetailsAction.OpenHumidor(humidor))
    }

    fun historyOpen() {
        sendEvent(CigarsDetailsAction.OpenHistory(cigar))
    }

    enum class InfoActions {
        None,
        CigarSize,
        CigarTobacco,
        CigarRatings,
    }

    fun moveCigar() {
        sendEvent(CigarsDetailsAction.MoveCigar(0))
    }

    fun showImages(selected: Int) {
        sendEvent(CigarsDetailsAction.ShowImages(cigar, selected))
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

    companion object Factory : ObjectFactory<CigarsDetailsScreenViewModel>() {
        override fun factory(data: Any?): CigarsDetailsScreenViewModel {
            return CigarsDetailsScreenViewModel(data as Cigar)
        }

    }

    sealed interface CigarsDetailsAction : CommonAction {
        data class AddToHumidor(val humidor: HumidorCigar) : CigarsDetailsAction
        data class AddCigarToHumidor(val humidor: Humidor, val count: Long, val price: Double? = null) : CigarsDetailsAction
        data class OpenHumidor(val humidor: Humidor) : CigarsDetailsAction
        data class OpenHistory(val cigar: Cigar) : CigarsDetailsAction
        data class MoveCigar(val dummy: Int) : CigarsDetailsAction
        data class ShowImages(val cigar: Cigar, val selected: Int) : CigarsDetailsAction
    }
}