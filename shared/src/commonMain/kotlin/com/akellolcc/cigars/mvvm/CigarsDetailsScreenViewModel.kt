/*
 * Copyright (C) 2024 Igor Kosulin
 * Last modified 4/19/24, 6:00 PM
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
import com.akellolcc.cigars.components.ValuePickerItem
import com.akellolcc.cigars.databases.RepositoryType
import com.akellolcc.cigars.databases.extensions.Cigar
import com.akellolcc.cigars.databases.extensions.CigarImage
import com.akellolcc.cigars.databases.extensions.Humidor
import com.akellolcc.cigars.databases.extensions.HumidorCigar
import com.akellolcc.cigars.databases.repository.CigarHumidorRepository
import com.akellolcc.cigars.databases.repository.CigarsRepository
import com.akellolcc.cigars.databases.repository.HistoryRepository
import com.akellolcc.cigars.databases.repository.HumidorsRepository
import com.akellolcc.cigars.databases.repository.ImagesRepository
import com.akellolcc.cigars.theme.Images
import com.badoo.reaktive.observable.ObservableWrapper
import com.badoo.reaktive.observable.map
import com.badoo.reaktive.observable.subscribe
import dev.icerock.moko.resources.desc.StringDesc


class CigarsDetailsScreenViewModel(private val cigar: Cigar) :
    DatabaseViewModel<Cigar, CigarsDetailsScreenViewModel.CigarsDetailsAction>() {
    private var cigarsDatabase: CigarsRepository = database.getRepository(RepositoryType.Cigars)
    private var humidorsRepository: HumidorsRepository =
        database.getRepository(RepositoryType.Humidors)
    private var imagesDatabase: ImagesRepository? = null
    private var cigarHumidorRepository: CigarHumidorRepository? = null
    private var cigarsHistoryDatabase: HistoryRepository? = null
    private var observeCigar: ObservableWrapper<Cigar>? = null
    private var _images: ObservableWrapper<List<CigarImage>>? = null
    private var _humidors: ObservableWrapper<List<HumidorCigar>>? = null

    var editing by mutableStateOf(cigar.rowid < 0)
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
    var myrating by mutableStateOf(cigar.myrating)
    var favorites by mutableStateOf(cigar.favorites)
    var notes by mutableStateOf(cigar.notes)
    var link by mutableStateOf(cigar.link)
    var count by mutableStateOf(cigar.count)

    var images by mutableStateOf(listOf<CigarImage>())
    var humidors by mutableStateOf(listOf<HumidorCigar>())


    var humidorCigarsCount by mutableStateOf<HumidorCigar?>(null)
    var infoDialog by mutableStateOf(InfoActions.None)
    var cigarRating by mutableStateOf(false)
    var moveCigarDialog by mutableStateOf(false)


    fun observeCigar() {
        if (cigar.rowid >= 0 && observeCigar == null) {
            imagesDatabase = database.getRepository(RepositoryType.CigarImages, cigar.rowid)
            cigarHumidorRepository =
                database.getRepository(RepositoryType.CigarHumidors, cigar.rowid)
            cigarsHistoryDatabase = database.getRepository(RepositoryType.CigarHistory, cigar.rowid)
            observeCigar = cigarsDatabase.observe(cigar.rowid)
            observeCigar?.map {
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
                    myrating = it.myrating
                    favorites = it.favorites
                    notes = it.notes
                    link = it.link
                    count = it.count
                }
            }?.subscribe()

            _images = imagesDatabase!!.all()
            _images?.map {
                images = it
            }?.subscribe()

            _humidors = cigarHumidorRepository!!.all()
            _humidors?.map {
                humidors = it
            }?.subscribe()
        }
    }

    fun humidorsCount(): Long {
        return database.numberOfEntriesIn(RepositoryType.Humidors)
    }

    fun humidors(): List<Humidor> {
        return humidorsRepository.allSync(null, true)
    }

    fun verifyFields(): Boolean {
        return name.isNotBlank()
                && brand?.isNotBlank() == true
                && country?.isNotBlank() == true
                && length.isNotBlank() && gauge != 0L
                && wrapper.isNotBlank()
                && binder.isNotBlank()
                && filler.isNotBlank()
    }

    fun saveEdit() {
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
            myrating = myrating,
            notes = notes,
            filler = filler,
            link = link,
            count = count,
            shopping = cigar.shopping,
            favorites = favorites,
            price = cigar.price,
        )
        cigarsDatabase.update(updated)
        editing = false
    }

    fun cancelEdit() {
        if (observeCigar != null) {
            observeCigar = null
            observeCigar()
        } else {
            sendEvent(CigarsDetailsAction.OnBackAction(0))
        }
        editing = false
    }

    fun rate() {
        cigarRating = true
    }

    fun favorite() {
        observeCigar?.let {
            cigarsDatabase.updateFavorite(!favorites, cigar).subscribe()
        }
    }

    fun addToHumidor(humidor: HumidorCigar) {
        sendEvent(CigarsDetailsAction.AddToHumidor(humidor))
    }

    fun openHumidor(humidor: Humidor) {
        sendEvent(CigarsDetailsAction.OpenHumidor(humidor))
    }

    fun historyOpen() {
        sendEvent(CigarsDetailsAction.OpenHistory(cigar))
    }

    fun updateCigarCount(entity: HumidorCigar, count: Long, price: Double? = null) {
        if (entity.count != count) {
            cigarHumidorRepository?.updateCount(entity, count, price)
            humidorCigarsCount = null
        }
    }

    fun updateCigarRating(rating: Long) {
        cigarsDatabase.updateRating(rating, cigar).subscribe()
    }

    fun moveFromHumidors(selected: Humidor?): List<ValuePickerItem<HumidorCigar>> {
        return humidors.map {
            ValuePickerItem(it, it.humidor.name, Images.tab_icon_humidors)
        }
    }

    fun moveToHumidors(selected: Humidor?): List<ValuePickerItem<Humidor>> {
        return humidorsRepository.allSync(null, true)
            .map { ValuePickerItem(it, it.name, Images.tab_icon_humidors) }
    }

    fun moveCigar(from: HumidorCigar, to: Humidor, count: Long) {
        cigarHumidorRepository?.moveCigar(from, to, count)?.subscribe {
            moveCigarDialog = false
        }
    }

    enum class InfoActions {
        None,
        CigarSize,
        CigarTobacco,
        CigarRatings,
    }

    fun openInfo(info: InfoActions) {
        infoDialog = info
    }

    fun moveCigar() {
        sendEvent(CigarsDetailsAction.MoveCigar(0))
    }

    fun showImages(selected: Int) {
        sendEvent(CigarsDetailsAction.ShowImages(cigar, selected))
    }

    sealed interface CigarsDetailsAction {
        data class OnBackAction(val dummy: Int) : CigarsDetailsAction
        data class AddToHumidor(val humidor: HumidorCigar) : CigarsDetailsAction
        data class OpenHumidor(val humidor: Humidor) : CigarsDetailsAction
        data class OpenHistory(val cigar: Cigar) : CigarsDetailsAction
        data class MoveCigar(val dummy: Int) : CigarsDetailsAction
        data class ShowImages(val cigar: Cigar, val selected: Int) : CigarsDetailsAction
        data class ShowError(val error: StringDesc) : CigarsDetailsAction
    }
}
