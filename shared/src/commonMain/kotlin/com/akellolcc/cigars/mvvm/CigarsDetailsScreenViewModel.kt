package com.akellolcc.cigars.mvvm

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.akellolcc.cigars.databases.RepositoryType
import com.akellolcc.cigars.databases.extensions.Cigar
import com.akellolcc.cigars.databases.extensions.CigarImage
import com.akellolcc.cigars.databases.extensions.CigarStrength
import com.akellolcc.cigars.databases.extensions.History
import com.akellolcc.cigars.databases.extensions.HistoryType
import com.akellolcc.cigars.databases.extensions.Humidor
import com.akellolcc.cigars.databases.extensions.HumidorCigar
import com.akellolcc.cigars.databases.extensions.ObservableEntity
import com.akellolcc.cigars.databases.repository.CigarHumidorRepository
import com.akellolcc.cigars.databases.repository.CigarsRepository
import com.akellolcc.cigars.databases.repository.HistoryRepository
import com.akellolcc.cigars.databases.repository.ImagesRepository
import com.akellolcc.cigars.logging.Log
import dev.icerock.moko.resources.desc.StringDesc
import kotlinx.datetime.Clock
import kotlin.math.absoluteValue


class CigarsDetailsScreenViewModel(private val cigar: Cigar) :
    DatabaseViewModel<Cigar, CigarsDetailsScreenViewModel.CigarsDetailsAction>() {
    private var cigarsDatabase: CigarsRepository? = null
    private var imagesDatabase: ImagesRepository? = null
    private var humidorsDatabase: CigarHumidorRepository? = null
    private var cigarsHistoryDatabase: HistoryRepository? = null
    private var observeCigar: ObservableEntity<Cigar>? = null
    private var _images: ObservableEntity<List<CigarImage>>? = null
    private var _humidors: ObservableEntity<List<HumidorCigar>>? = null

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

    init {
        if (cigar.rowid >= 0) {
            observeCigar()
        }
    }

    private fun observeCigar() {
        cigarsDatabase = database.getRepository(RepositoryType.Cigars)
        imagesDatabase = database.getRepository(RepositoryType.CigarImages, cigar.rowid)
        humidorsDatabase = database.getRepository(RepositoryType.CigarHumidors, cigar.rowid)
        cigarsHistoryDatabase = database.getRepository(RepositoryType.CigarHistory, cigar.rowid)

        observeCigar = ObservableEntity(cigarsDatabase!!.observe(cigar.rowid))
        observeCigar?.map { if (!editing) name = it?.name ?: "" }
        observeCigar?.map { if (!editing) brand = it?.brand }
        observeCigar?.map { if (!editing) country = it?.country }
        observeCigar?.map { if (!editing) shape = it?.cigar ?: "" }
        observeCigar?.map { if (!editing) length = it?.length ?: "" }
        observeCigar?.map { if (!editing) gauge = it?.gauge ?: 0 }
        observeCigar?.map { if (!editing) wrapper = it?.wrapper ?: "" }
        observeCigar?.map { if (!editing) binder = it?.binder ?: "" }
        observeCigar?.map { if (!editing) filler = it?.filler ?: "" }
        observeCigar?.map { if (!editing) strength = it?.strength ?: CigarStrength.Mild }
        observeCigar?.map { if (!editing) rating = it?.rating }
        observeCigar?.map { if (!editing) myrating = it?.myrating }
        observeCigar?.map { if (!editing) favorites = it?.favorites ?: false }
        observeCigar?.map { if (!editing) notes = it?.notes }
        observeCigar?.map { if (!editing) link = it?.link }
        observeCigar?.map { if (!editing) count = it?.count ?:0 }

        _images = ObservableEntity(imagesDatabase!!.observeAll())
        _images?.map {
            images = it ?: listOf()
        }
        _humidors = ObservableEntity(humidorsDatabase!!.observeAll())
        _humidors?.map {
            humidors = it ?: listOf()
        }
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
        cigarsDatabase?.update(updated)
        editing = false
    }

    fun cancelEdit() {
        editing = false
        if (cigarsDatabase != null) {
            observeCigar?.reload()
        } else {
            sendEvent(CigarsDetailsAction.OnBackAction(0))
        }
    }

    fun rate() {
        sendEvent(CigarsDetailsAction.RateCigar(0))
    }

    fun favorite() {
        observeCigar?.value?.let {
            val updated = cigar.copy(favorites = !it.favorites)
            cigarsDatabase?.update(updated)
            Log.debug("cigar.favorites = ${it.favorites}")
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
        val c = entity.count - count
        if (c != 0L) {
            humidorsDatabase?.update(entity.copy(count = count))
            val type = if (c < 0) HistoryType.Addition else HistoryType.Deletion
            cigarsHistoryDatabase?.add(
                History(
                    -1,
                    c.absoluteValue,
                    Clock.System.now().toEpochMilliseconds(),
                    count,
                    price,
                    type,
                    cigar.rowid,
                    entity.humidor!!.rowid
                ),
                null
            )
            humidorCigarsCount = null
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

    sealed interface CigarsDetailsAction {
        data class OnBackAction(val dummy: Int) : CigarsDetailsAction
        data class RateCigar(val dummy: Int) : CigarsDetailsAction
        data class AddToHumidor(val humidor: HumidorCigar) : CigarsDetailsAction
        data class OpenHumidor(val humidor: Humidor) : CigarsDetailsAction
        data class OpenHistory(val cigar: Cigar) : CigarsDetailsAction
        data class MoveCigar(val dummy: Int) : CigarsDetailsAction
        data class ShowError(val error: StringDesc) : CigarsDetailsAction
    }
}
