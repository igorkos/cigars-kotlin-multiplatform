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

import cafe.adriel.voyager.core.concurrent.ThreadSafeMap
import cafe.adriel.voyager.core.model.ScreenModel
import kotlin.reflect.KClass

abstract class ViewModelsFactory<T : ScreenModel> {
    abstract fun factory(data: Any? = null): T
}

object ViewModelRegistry {

    @PublishedApi
    internal val factories: ThreadSafeMap<KClass<*>, ViewModelsFactory<*>> = ThreadSafeMap()

    inline fun <reified T : ScreenModel> register(
        modelKClass: KClass<out T>,
        factory: ViewModelsFactory<T>
    ) {
        factories[T::class] = factory
    }

    inline fun <reified T : ScreenModel> create(modelKClass: KClass<out T>, data: Any? = null): T {
        if (factories.containsKey(modelKClass)) {
            val factory: ViewModelsFactory<T> = factories[modelKClass] as ViewModelsFactory<T>
            return factory.factory(data)
        }
        throw IllegalArgumentException("ViewModel not found")
    }
}

inline fun <reified T : ScreenModel> createViewModel(
    modelKClass: KClass<out T>,
    data: Any? = null
): T = ViewModelRegistry.create(modelKClass, data)


