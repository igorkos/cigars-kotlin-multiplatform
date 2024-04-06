package com.akellolcc.cigars.mvvm

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.akellolcc.cigars.databases.extensions.CigarImage
import com.akellolcc.cigars.databases.extensions.Humidor
import com.akellolcc.cigars.databases.extensions.HumidorCigar
import com.akellolcc.cigars.databases.extensions.ObservableEntity
import com.akellolcc.cigars.databases.repository.impl.SqlDelightHumidorImagesRepository
import com.akellolcc.cigars.databases.repository.impl.SqlDelightHumidorsRepository
import dev.icerock.moko.resources.desc.StringDesc

class HumidorDetailsScreenViewModel(private val humidor: Humidor) :
    ActionsViewModel<HumidorDetailsScreenViewModel.Action>() {
    private var imagesDatabase: SqlDelightHumidorImagesRepository? = null
    private var humidorsDatabase: SqlDelightHumidorsRepository? = null
    private var observeHumidor: ObservableEntity<Humidor>? = null
    private var _images: ObservableEntity<List<CigarImage>>? = null

    var loading by mutableStateOf(false)
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
            observeCigar()
        }
    }

    private fun observeCigar() {
        imagesDatabase = SqlDelightHumidorImagesRepository(humidor.rowid)
        humidorsDatabase = SqlDelightHumidorsRepository()
        observeHumidor = ObservableEntity(humidorsDatabase!!.observe(humidor.rowid))

        observeHumidor?.map { if (!editing) name = it?.name ?: "" }
        observeHumidor?.map { if (!editing) brand = it?.brand ?: "" }
        observeHumidor?.map { if (!editing) holds = it?.holds ?: 0 }
        observeHumidor?.map { if (!editing) count = it?.count ?: 0 }
        observeHumidor?.map { if (!editing) temperature = it?.temperature ?: 0 }
        observeHumidor?.map { if (!editing) humidity = it?.humidity ?: 0.0 }
        observeHumidor?.map { if (!editing) notes = it?.notes }
        observeHumidor?.map { if (!editing) link = it?.link }
        observeHumidor?.map { if (!editing) type = it?.type }

        _images = ObservableEntity(imagesDatabase!!.observeAll())
        _images?.map {
            images = it ?: listOf()
        }
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
        humidorsDatabase?.update(updated)
        editing = false
    }

    fun cancelEdit() {
        editing = false
        if (humidorsDatabase != null) {
            observeHumidor?.reload()
        } else {
            sendEvent(Action.OnBackAction(0))
        }
    }

    sealed interface Action {
        data class OnBackAction(val dummy: Int) : Action
        data class ShowError(val error: StringDesc) : Action
    }
}
