package com.akellolcc.cigars.mvvm

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.akellolcc.cigars.databases.Database
import com.akellolcc.cigars.databases.extensions.Cigar
import com.akellolcc.cigars.databases.extensions.CigarImage
import com.akellolcc.cigars.databases.extensions.CigarStrength
import com.akellolcc.cigars.databases.extensions.Humidor
import com.akellolcc.cigars.databases.extensions.HumidorCigar
import com.akellolcc.cigars.databases.extensions.ObservableEntity
import com.akellolcc.cigars.databases.repository.impl.SqlDelightCigarHumidorRepository
import com.akellolcc.cigars.databases.repository.impl.SqlDelightCigarImagesRepository
import com.akellolcc.cigars.databases.repository.impl.SqlDelightCigarsRepository
import com.akellolcc.cigars.logging.Log
import dev.icerock.moko.resources.desc.StringDesc

class CigarsDetailsScreenViewModel(private val cigar: Cigar) : ActionsViewModel<CigarsDetailsScreenViewModel.CigarsDetailsAction>()  {
    private val cigarsDatabase: SqlDelightCigarsRepository = SqlDelightCigarsRepository(Database.getInstance().dbQueries)
    private val imagesDatabase: SqlDelightCigarImagesRepository = SqlDelightCigarImagesRepository(cigar.rowid, Database.getInstance().dbQueries)
    private val humidorsDatabase: SqlDelightCigarHumidorRepository = SqlDelightCigarHumidorRepository(cigar.rowid, Database.getInstance().dbQueries)

    var loading by mutableStateOf(false)
    var editing by mutableStateOf(false)

    private val observeCigar = ObservableEntity(cigarsDatabase.observe(cigar.rowid))
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

    init {
        observeCigar.map { if (!editing) name = it?.name ?: "" }
        observeCigar.map { if (!editing) brand = it?.brand }
        observeCigar.map { if (!editing) country = it?.country }
        observeCigar.map { if (!editing) shape = it?.cigar ?: "" }
        observeCigar.map { if (!editing) length = it?.length ?: "" }
        observeCigar.map { if (!editing) gauge = it?.gauge ?: 0 }
        observeCigar.map { if (!editing) wrapper = it?.wrapper ?: "" }
        observeCigar.map { if (!editing) binder = it?.binder ?: "" }
        observeCigar.map { if (!editing) filler = it?.filler ?: "" }
        observeCigar.map { if (!editing) strength = it?.strength ?: CigarStrength.Mild }
        observeCigar.map { if (!editing) rating = it?.rating }
        observeCigar.map { if (!editing) myrating = it?.myrating }
        observeCigar.map { if (!editing) favorites = it?.favorites ?: false }
        observeCigar.map { if (!editing) notes = it?.notes }
        observeCigar.map { if (!editing) link = it?.link }
    }
    @Composable
    fun humidorsAsState()  : State<List<HumidorCigar>> {
        return humidorsDatabase.observeAll().collectAsState(listOf())
    }
    @Composable
    fun imagesAsState() : State<List<CigarImage>> {
        return imagesDatabase.observeAll().collectAsState(listOf())
    }

    fun saveEdit(){
        val updated = Cigar(cigar.rowid,
            name = name,
            brand = brand,
            country=country,
            date=cigar.date,
            cigar=shape,
            wrapper=wrapper,
            binder=binder,
            gauge=gauge,
            length=length,
            strength=strength,
            rating= rating,
            myrating=myrating,
            notes=notes,
            filler=filler,
            link=link,
            shopping=cigar.shopping,
            favorites=favorites)
        cigarsDatabase.update(updated)
        editing = false
    }

    fun cancelEdit(){
        editing = false
        observeCigar.reload()
    }

    fun rate(){
        sendEvent(CigarsDetailsAction.RateCigar(0))
    }

    fun favorite(){
        observeCigar.value?.let {
            val updated = cigar.copy(favorites = !it.favorites)
            cigarsDatabase.update(updated)
            Log.debug("cigar.favorites = ${it.favorites}")
        }
    }

    fun addToHumidor(humidor: Humidor){
        sendEvent(CigarsDetailsAction.AddToHumidor(humidor))
    }

    fun openHumidor(humidor: Humidor){
        sendEvent(CigarsDetailsAction.OpenHumidor(humidor))
    }
    sealed interface CigarsDetailsAction {
        data class RateCigar(val dummy: Int) : CigarsDetailsAction
        data class AddToHumidor(val humidor: Humidor) : CigarsDetailsAction
        data class OpenHumidor(val humidor: Humidor) : CigarsDetailsAction
        data class ShowError(val error: StringDesc) : CigarsDetailsAction
    }
}
