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
import com.akellolcc.cigars.databases.extensions.Humidor
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

    private val observeCigar = ObservableEntity(cigarsDatabase.observe(cigar.rowid))
    val name by mutableStateOf(cigar.name)
    val brand by observeCigar.map { it?.brand }
    val country by observeCigar.map { it?.country }
    val shape by observeCigar.map { it?.cigar }
    val length by observeCigar.map { it?.length }
    val gauge by observeCigar.map { it?.gauge }
    val wrapper by observeCigar.map { it?.wrapper }
    val binder by observeCigar.map { it?.binder }
    val filler by observeCigar.map { it?.filler }
    val strength by observeCigar.map { it?.strength }
    val rating by observeCigar.map { it?.rating }
    val myrating by observeCigar.map { it?.myrating }
    val favorites by observeCigar.map { it?.favorites }
    val notes by observeCigar.map { it?.notes }
    val link by observeCigar.map { it?.link }

    @Composable
    fun humidorsAsState()  : State<List<Humidor>> {
        val a  by observeCigar.asState()
        return humidorsDatabase.observeAll().collectAsState(listOf())
    }
    @Composable
    fun imagesAsState() : State<List<CigarImage>> {
        return imagesDatabase.observeAll().collectAsState(listOf())
    }

    fun rate(){
        sendEvent(CigarsDetailsAction.RateCigar(0))
    }

    fun favorite(){
        observeCigar.entity.value?.let {
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
