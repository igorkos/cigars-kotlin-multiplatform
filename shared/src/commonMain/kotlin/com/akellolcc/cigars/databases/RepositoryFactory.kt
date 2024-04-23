/*
 * Copyright (C) 2024 Igor Kosulin
 * Last modified 4/23/24, 3:40 PM
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

package com.akellolcc.cigars.databases

import cafe.adriel.voyager.core.concurrent.ThreadSafeMap
import com.akellolcc.cigars.databases.repository.Repository
import kotlin.reflect.KClass

abstract class RepositoryFactory<T : Repository<*>> {
    abstract fun factory(data: Any? = null): T
}

object RepositoryRegistry {

    @PublishedApi
    internal val factories: ThreadSafeMap<KClass<*>, RepositoryFactory<*>> = ThreadSafeMap()

    inline fun <reified T : Repository<*>> register(
        modelKClass: KClass<*>,
        factory: RepositoryFactory<T>
    ) {
        factories[modelKClass] = factory
    }

    inline fun <reified T : Repository<*>> create(
        modelKClass: KClass<out T>,
        data: Any? = null
    ): T {
        if (factories.containsKey(modelKClass)) {
            val factory: RepositoryFactory<T> = factories[modelKClass] as RepositoryFactory<T>
            return factory.factory(data)
        }
        throw IllegalArgumentException("Repository for ${modelKClass.simpleName} not found")
    }
}

inline fun <reified T : Repository<*>> createRepository(
    modelKClass: KClass<out T>,
    data: Any? = null
): T = RepositoryRegistry.create(modelKClass, data)


