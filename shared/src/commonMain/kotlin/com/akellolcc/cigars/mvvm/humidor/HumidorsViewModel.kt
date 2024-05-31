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
import com.akellolcc.cigars.databases.models.Humidor
import com.akellolcc.cigars.databases.repository.HumidorsRepository
import com.akellolcc.cigars.mvvm.base.BaseListViewModel
import com.akellolcc.cigars.utils.ObjectFactory
import dev.icerock.moko.resources.desc.StringDesc


class HumidorsViewModel : BaseListViewModel<Humidor>() {
    override val repository: HumidorsRepository = createRepository(HumidorsRepository::class)

    override fun entitySelected(entity: Humidor) {
        sendEvent(Action.RouteToHumidor(entity))
    }

    fun addHumidor() {
        sendEvent(Action.AddHumidor(null))
    }

    companion object Factory : ObjectFactory<HumidorsViewModel>() {
        override fun factory(data: Any?): HumidorsViewModel {
            return HumidorsViewModel()
        }
    }

    sealed interface Action {
        data class RouteToHumidor(val humidor: Humidor) : Action
        data class AddHumidor(val dummy: Any?) : Action
        data class ShowError(val error: StringDesc) : Action
    }
}