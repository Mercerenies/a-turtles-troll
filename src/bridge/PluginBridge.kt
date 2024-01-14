
package com.mercerenies.turtletroll.bridge

import org.bukkit.Bukkit
import org.bukkit.NamespacedKey
import org.bukkit.plugin.Plugin

// Parent class for bridge classes to optional dependencies. Each
// plugin that we soft-depend on (including Raccoon Mischief, which is
// not in plugin.yml since it's a mutual dependency) has a
// PluginBridge associated to it, which can lazily access the relevant
// plugin if it exists in the runtime.
abstract class PluginBridge() {

  abstract val pluginName: String

  val plugin: Plugin? by lazy {
    Bukkit.getServer().getPluginManager().getPlugin(pluginName)
  }

  fun exists(): Boolean =
    plugin != null

  fun assertExists(errorMessage: String = "${pluginName} does not exist") {
    if (!exists()) {
      throw PluginNotFoundException(errorMessage)
    }
  }

  fun namespacedKey(key: String): NamespacedKey? =
    plugin?.let { NamespacedKey(it, key) }

}
