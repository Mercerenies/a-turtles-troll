
package com.mercerenies.turtletroll.feature.builder

import com.mercerenies.turtletroll.storage.GlobalDataHolder

import org.bukkit.plugin.Plugin

import kotlin.reflect.KClass

// A feature builder has access to the plugin it's building for, as
// well as some persistent data store.
interface BuilderState {
  val plugin: Plugin
  val dataStore: GlobalDataHolder

  fun registerSharedData(key: String, value: Any)

  fun<T : Any> getSharedData(key: String, targetType: KClass<T>): T?

}
