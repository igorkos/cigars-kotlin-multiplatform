/*******************************************************************************************************************************************
 * Copyright (C) 2024 Igor Kosulin
 * Last modified 6/18/24, 12:25 PM
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
import com.akellolcc.cigars.databases.createRepository
import com.akellolcc.cigars.databases.models.Cigar
import com.akellolcc.cigars.databases.models.Humidor
import com.akellolcc.cigars.databases.models.HumidorCigar
import com.akellolcc.cigars.databases.repository.CigarHistoryRepository
import com.akellolcc.cigars.databases.repository.CigarHumidorRepository
import com.akellolcc.cigars.databases.repository.CigarHumidorsRepository
import com.akellolcc.cigars.databases.repository.CigarImagesRepository
import com.akellolcc.cigars.databases.repository.CigarsRepository
import com.akellolcc.cigars.databases.repository.HumidorsRepository
import com.akellolcc.cigars.logging.Log
import com.akellolcc.cigars.mvvm.base.BaseDetailsScreenViewModel
import com.akellolcc.cigars.screens.components.ValuePickerItem
import com.akellolcc.cigars.theme.Images
import com.akellolcc.cigars.utils.ObjectFactory


class CigarsDetailsScreenViewModel(cigar: Cigar, var humidor: Humidor? = null) : BaseDetailsScreenViewModel<Cigar>(cigar) {
    private var cigarsRepository: CigarsRepository = createRepository(CigarsRepository::class)
    private var humidorsRepository: HumidorsRepository = createRepository(HumidorsRepository::class)
    private var imagesRepository: CigarImagesRepository? = null
    private var cigarHumidorRepository: CigarHumidorRepository? = null
    private var cigarHistoryRepository: CigarHistoryRepository? = null

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
    var price by mutableStateOf(cigar.price)

    var humidors by mutableStateOf(listOf<HumidorCigar>())


    var humidorCigarsCount by mutableStateOf<HumidorCigar?>(null)
    var infoDialog by mutableStateOf(InfoActions.None)
    var cigarRating by mutableStateOf(false)
    var moveCigarDialog by mutableStateOf(false)

    /**
     * Observing cigar state
     */
    override fun observe() {
        imagesRepository = createRepository(CigarImagesRepository::class, entity.rowid)
        cigarHumidorRepository = createRepository(CigarHumidorsRepository::class, entity.rowid)
        cigarHistoryRepository = createRepository(CigarHistoryRepository::class, entity.rowid)
        disposable += execute(cigarsRepository.observe(entity.rowid)) {
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
    }

    /**
     * Editing cigar
     */
    override fun verifyFields(): Boolean {
        return name.isNotBlank()
                && brand?.isNotBlank() == true
                && country?.isNotBlank() == true
                && length.isNotBlank() && gauge != 0L
                && wrapper.isNotBlank()
                && binder.isNotBlank()
                && filler.isNotBlank()
    }

    override fun onSaveEdit() {
        val updated = Cigar(
            entity.rowid,
            name = name,
            brand = brand,
            country = country,
            date = entity.date,
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
            shopping = entity.shopping,
            favorites = favorites,
            price = entity.price,
        )
        if (updated.rowid < 0) {
            entity = updated
            addCigar(humidor!!, count, price) {
                entity = it
                observe()
            }
        } else {
            executeQuery(cigarsRepository.update(updated)) {
                editing = false
            }
        }
    }

    fun addCigar(humidor: Humidor, count: Long, price: Double? = null, onAdded: ((Cigar) -> Unit)? = null) {
        entity.count = count
        entity.price = price
        executeQuery(cigarsRepository.add(entity, humidor)) {
            editing = false
            moveCigarDialog = false
            onAdded?.invoke(it)
        }
    }

    /**
     *  Updating cigar no edit mode
     */
    fun favorite() {
        if (disposable.isNotEmpty()) {
            executeQuery(
                cigarsRepository.updateFavorite(!favorites, entity)
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
            cigarsRepository.updateRating(rating, entity)
        )
    }

    /**
     * Moving cigars cigar to humidor
     */
    fun humidorsCount(): Long {
        val repo = createRepository(HumidorsRepository::class)
        return repo.count()
    }

    fun moveFromHumidors(): List<ValuePickerItem> {
        return humidors.map {
            ValuePickerItem(it, it.humidor.name, Images.tab_icon_humidors)
        }
    }

    fun moveToHumidors(from: Humidor?): List<ValuePickerItem> {
        val allHumidors = humidorsRepository.allSync()
        val to = if (from == null) allHumidors else allHumidors - from
        return to.map { ValuePickerItem(it, it.name, Images.tab_icon_humidors) }
    }

    fun moveCigar(from: HumidorCigar, to: Humidor, count: Long) {
        Log.debug("Move cigar from ${from.humidor.name} to ${to.name} -> $count")
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

    enum class InfoActions {
        None,
        CigarSize,
        CigarTobacco,
        CigarRatings,
    }

    fun moveCigar() {
        sendEvent(CigarsDetailsAction.MoveCigar(0))
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
        data class MoveCigar(val dummy: Int) : CigarsDetailsAction
    }
}
