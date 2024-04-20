/*
 * Copyright (C) 2024 Igor Kosulin
 * Last modified 4/19/24, 11:45 PM
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

package com.akellolcc.cigars.screens.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import com.akellolcc.cigars.theme.imagePainter

interface ITabItem : Tab {
    val route: NavRoute
    override val options: TabOptions
        @Composable
        get() {
            val icon = imagePainter(route.icon)
            return remember {
                TabOptions(
                    index = 0u,
                    title = route.title,
                    icon = icon
                )
            }
        }
}
