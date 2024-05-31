/*******************************************************************************************************************************************
 * Copyright (C) 2024 Igor Kosulin
 * Last modified 5/31/24, 10:41 AM
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

package com.akellolcc.cigars.mvvm.humidor

import com.akellolcc.cigars.databases.createRepository
import com.akellolcc.cigars.databases.models.Cigar
import com.akellolcc.cigars.databases.models.Humidor
import com.akellolcc.cigars.databases.models.HumidorCigar
import com.akellolcc.cigars.databases.repository.HumidorCigarsRepository
import com.akellolcc.cigars.mvvm.base.BaseListViewModel
import com.akellolcc.cigars.utils.ObjectFactory


class HumidorCigarsScreenViewModel(val humidor: Humidor) : BaseListViewModel<HumidorCigar>() {
    override val repository: HumidorCigarsRepository =
        createRepository(HumidorCigarsRepository::class, humidor.rowid)

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

    companion object Factory : ObjectFactory<HumidorCigarsScreenViewModel>() {
        override fun factory(data: Any?): HumidorCigarsScreenViewModel {
            return HumidorCigarsScreenViewModel(data as Humidor)
        }

    }

    sealed interface CigarsAction : CommonAction {
        data class RouteToHumidorDetails(val humidor: Humidor) : CigarsAction
        data class RouteToCigar(val cigar: Cigar) : CigarsAction
        data class AddCigar(val cigar: Cigar? = null) : CigarsAction
        data class OpenHistory(val dummy: Int) : CigarsAction
    }
}