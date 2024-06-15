/*******************************************************************************************************************************************
 * Copyright (C) 2024 Igor Kosulin
 * Last modified 6/14/24, 11:04 AM
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

package com.akellolcc.cigars.utils

import cafe.adriel.voyager.core.concurrent.ThreadSafeMap
import kotlin.reflect.KClass

abstract class ObjectFactory<T> {
    abstract fun factory(data: Any? = null): T
}

@Suppress("UNCHECKED_CAST")
open class ObjectRegistry<F> {
    private val factories: ThreadSafeMap<KClass<*>, ObjectFactory<F>> = ThreadSafeMap()
    private val sharedFactories: ThreadSafeMap<KClass<*>, ObjectFactory<F>> = ThreadSafeMap()
    private var sharedObjects: ThreadSafeMap<KClass<*>, F> = ThreadSafeMap()
    fun register(
        modelKClass: KClass<*>,
        factory: ObjectFactory<*>,
        shared: Boolean = false
    ) {
        if (shared) {
            sharedFactories[modelKClass] = factory as ObjectFactory<F>
        } else {
            factories[modelKClass] = factory as ObjectFactory<F>
        }
    }

    fun create(
        modelKClass: KClass<*>,
        data: Any? = null
    ): F {
        //Log.debug("Registry creating $modelKClass")
        if (factories.containsKey(modelKClass)) {
            val factory = factories[modelKClass] as ObjectFactory<F>
            return factory.factory(data)
        } else if (sharedFactories.containsKey(modelKClass)) {
            if (sharedObjects.containsKey(modelKClass)) {
                return sharedObjects[modelKClass] as F
            }
            val factory = sharedFactories[modelKClass] as ObjectFactory<F>
            sharedObjects[modelKClass] = factory.factory(data)
            return sharedObjects[modelKClass] as F
        }
        throw IllegalArgumentException("No factory not found for $modelKClass")
    }
}

