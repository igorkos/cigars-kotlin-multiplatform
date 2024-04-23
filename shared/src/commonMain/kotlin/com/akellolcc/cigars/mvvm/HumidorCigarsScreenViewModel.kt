/*
 * Copyright (C) 2024 Igor Kosulin
 * Last modified 4/23/24, 1:07 PM
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

import com.akellolcc.cigars.databases.RepositoryType
import com.akellolcc.cigars.databases.extensions.Cigar
import com.akellolcc.cigars.databases.extensions.Humidor
import com.akellolcc.cigars.databases.extensions.HumidorCigar
import com.akellolcc.cigars.databases.repository.CigarHumidorRepository
import dev.icerock.moko.resources.desc.StringDesc


class HumidorCigarsScreenViewModel(val humidor: Humidor) :
    BaseListViewModel<HumidorCigar, HumidorCigarsScreenViewModel.CigarsAction>() {
    override val repository: CigarHumidorRepository =
        database.getRepository(RepositoryType.HumidorCigars, humidor.rowid)

    override fun entitySelected(entity: HumidorCigar) {
        sendEvent(CigarsAction.RouteToCigar(entity.cigar))
    }

    fun humidorDetails() {
        sendEvent(CigarsAction.RouteToHumidorDetails(humidor))
    }

    fun addCigar() {
        sendEvent(CigarsAction.AddCigar())
    }

    fun openHistory() {
        sendEvent(CigarsAction.OpenHistory(1))
    }

    companion object Factory : ViewModelsFactory<HumidorCigarsScreenViewModel>() {
        override fun factory(data: Any?): HumidorCigarsScreenViewModel {
            return HumidorCigarsScreenViewModel(data as Humidor)
        }

    }

    sealed interface CigarsAction {
        data class RouteToHumidorDetails(val humidor: Humidor) : CigarsAction
        data class RouteToCigar(val cigar: Cigar) : CigarsAction
        data class AddCigar(val cigar: Cigar? = null) : CigarsAction
        data class OpenHistory(val dummy: Int) : CigarsAction
        data class ShowError(val error: StringDesc) : CigarsAction
    }
}