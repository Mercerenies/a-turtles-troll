
package com.mercerenies.turtletroll.bridge

import com.mercerenies.turtletroll.util.ResettableLazy

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

  private val lazyPlugin = ResettableLazy {
    Bukkit.getServer().getPluginManager().getPlugin(pluginName)
  }

  val plugin: Plugin?
    get() = lazyPlugin.value

  fun exists(): Boolean =
    plugin != null

  fun assertExists(errorMessage: String = "${pluginName} does not exist") {
    if (!exists()) {
      throw PluginNotFoundException(errorMessage)
    }
  }

  fun resetLazyState() {
    lazyPlugin.reset()
  }

  fun namespacedKey(key: String): NamespacedKey? =
    plugin?.let { NamespacedKey(it, key) }

}
