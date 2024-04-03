package com.akellolcc.cigars.mvvm

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.akellolcc.cigars.camera.SharedImage
import com.akellolcc.cigars.databases.Database
import com.akellolcc.cigars.databases.extensions.Cigar
import com.akellolcc.cigars.databases.extensions.CigarImage
import com.akellolcc.cigars.databases.repository.impl.SqlDelightCigarImagesRepository
import dev.icerock.moko.resources.desc.StringDesc


class ImagesViewScreenViewModel(val cigar: Cigar) : ActionsViewModel<ImagesViewScreenViewModel.Action>() {
    private val database: SqlDelightCigarImagesRepository = SqlDelightCigarImagesRepository(cigar.rowid, Database.getInstance().dbQueries)
    var loading by mutableStateOf(false)
    fun addImage(image: SharedImage) {
        image.toByteArray()?.let {
            database.add(CigarImage(-1, data_ = it))
        }
    }

    @Composable
    fun asState() : State<List<CigarImage>> {
        return database.observeAll().collectAsState(listOf())
    }

    sealed interface Action {
        data class ShowError(val error: StringDesc) : Action
    }
}