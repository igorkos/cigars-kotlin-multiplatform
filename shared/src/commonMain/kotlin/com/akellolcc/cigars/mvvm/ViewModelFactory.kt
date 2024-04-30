/*******************************************************************************************************************************************
 * Copyright (C) 2024 Igor Kosulin
 * Last modified 4/29/24, 8:49 PM
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

package com.akellolcc.cigars.mvvm

import cafe.adriel.voyager.core.model.ScreenModel
import com.akellolcc.cigars.utils.ObjectRegistry
import kotlin.reflect.KClass

object ViewModelRegistry : ObjectRegistry<ScreenModel>()

inline fun <reified T : ScreenModel> createViewModel(
    modelKClass: KClass<out T>,
    data: Any? = null
): T = ViewModelRegistry.create(modelKClass, data) as T

