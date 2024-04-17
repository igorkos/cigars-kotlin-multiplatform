package com.akellolcc.cigars.mvvm

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.akellolcc.cigars.databases.RepositoryType
import com.akellolcc.cigars.databases.extensions.CigarImage
import com.akellolcc.cigars.databases.extensions.Humidor
import com.akellolcc.cigars.databases.extensions.HumidorCigar
import com.akellolcc.cigars.databases.repository.HumidorsRepository
import com.akellolcc.cigars.databases.repository.ImagesRepository
import com.badoo.reaktive.observable.ObservableWrapper
import com.badoo.reaktive.observable.map
import com.badoo.reaktive.observable.subscribe
import dev.icerock.moko.resources.desc.StringDesc

class HumidorDetailsScreenViewModel(private var humidor: Humidor) :
    DatabaseViewModel<Humidor, HumidorDetailsScreenViewModel.Action>() {
    private var imagesDatabase: ImagesRepository? = null
    private var humidorsDatabase: HumidorsRepository? =
        database.getRepository(RepositoryType.Humidors)
    private var observeHumidor: ObservableWrapper<Humidor>? = null
    private var _images: ObservableWrapper<List<CigarImage>>? = null

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
    var images by mutableStateOf(listOf<CigarImage>())
    var humidors by mutableStateOf(listOf<HumidorCigar>())


    init {
        if (humidor.rowid >= 0) {
            observeHumidor()
        }
    }

    private fun observeHumidor() {
        imagesDatabase = database.getRepository(RepositoryType.HumidorImages, humidor.rowid)
        observeHumidor = humidorsDatabase!!.observe(humidor.rowid)
        observeHumidor?.map {
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
        }?.subscribe()

        _images = imagesDatabase!!.all()
        _images?.map {
            images = it
        }?.subscribe()
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
            sendEvent(Action.OnBackAction(0))
        }
    }

    sealed interface Action {
        data class OnBackAction(val dummy: Int) : Action
        data class ShowError(val error: StringDesc) : Action
    }
}
