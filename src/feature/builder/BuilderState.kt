
package com.mercerenies.turtletroll.feature.builder

import com.mercerenies.turtletroll.storage.GlobalDataHolder

import org.bukkit.plugin.Plugin

// A feature builder has access to the plugin it's building for, as
// well as some persistent data store.
interface BuilderState {
  val plugin: Plugin
  val dataStore: GlobalDataHolder
}
