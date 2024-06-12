/*******************************************************************************************************************************************
 * Copyright (C) 2024 Igor Kosulin
 * Last modified 6/11/24, 7:37 PM
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
import com.akellolcc.cigars.databases.models.CigarSortingFields
import com.akellolcc.cigars.databases.models.Humidor
import com.akellolcc.cigars.databases.models.HumidorCigar
import com.akellolcc.cigars.databases.repository.HumidorCigarsRepository
import com.akellolcc.cigars.mvvm.base.BaseListViewModel
import com.akellolcc.cigars.screens.components.search.data.CigarFilterParameters
import com.akellolcc.cigars.screens.components.search.data.CigarSortingParameters
import com.akellolcc.cigars.screens.components.search.data.FilterParameter
import com.akellolcc.cigars.theme.Images
import com.akellolcc.cigars.theme.Localize
import com.akellolcc.cigars.utils.ObjectFactory


class HumidorCigarsScreenViewModel(val humidor: Humidor) : BaseListViewModel<HumidorCigar>() {
    init {
        sortField = FilterParameter(CigarSortingFields.Name.value, true)
        sortingFields = CigarSortingParameters()
        searchingFields = CigarFilterParameters()
        topBarMenu = (sortingFields as CigarSortingParameters).selected.append(
            listOf(
                FilterParameter(
                    Localize.cigar_details_top_bar_history_desc, true,
                    Localize.title_humidor_history,
                    Images.icon_menu_history,
                    false
                ),
                FilterParameter(
                    Localize.cigar_details_top_bar_info_desc, true,
                    Localize.title_humidor_details_desc,
                    Images.icon_menu_info,
                    false
                ),
                FilterParameter(Localize.cigar_details_top_bar_divider_desc, true, "", selectable = false),
                FilterParameter(
                    Localize.screen_list_filter_action_descr,
                    true,
                    Localize.screen_list_filter_action_descr,
                    Images.tab_icon_search,
                    false
                ),
                FilterParameter(Localize.cigar_details_top_bar_divider_desc, true, "", selectable = false),
                sortOrderField,
                FilterParameter(Localize.cigar_details_top_bar_divider_desc, true, "", selectable = false)
            ),
            false
        )
    }

    override val repository: HumidorCigarsRepository =
        createRepository(HumidorCigarsRepository::class, humidor.rowid)

    override fun entitySelected(entity: HumidorCigar) {
        sendEvent(CigarsAction.RouteToCigar(entity.cigar))
    }

    override fun updateSearch(value: Boolean) {
        searchingFields = CigarFilterParameters()
        search = value
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