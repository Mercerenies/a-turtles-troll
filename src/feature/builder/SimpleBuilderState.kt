
package com.mercerenies.turtletroll.feature.builder

import com.mercerenies.turtletroll.storage.GlobalDataHolder
import com.mercerenies.turtletroll.config.ConfigOptions

import org.bukkit.plugin.Plugin

import kotlin.collections.HashMap
import kotlin.reflect.KClass
import kotlin.reflect.safeCast

class SimpleBuilderState(
  override val plugin: Plugin,
  override val dataStore: GlobalDataHolder,
  override val config: ConfigOptions,
) : BuilderState {

  private val storage: HashMap<String, Any> = HashMap()

  override fun registerSharedData(key: String, value: Any) {
    storage.put(key, value)
  }

  override fun<T : Any> getSharedData(key: String, targetType: KClass<T>): T? {
    val value = storage.get(key)
    if (value == null) {
      return null
    }
    return targetType.safeCast(value)
  }

}
