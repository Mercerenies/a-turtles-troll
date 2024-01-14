
package com.mercerenies.turtletroll.bridge

import org.bukkit.Bukkit
import org.bukkit.NamespacedKey
import org.bukkit.plugin.Plugin

object RaccoonBridge {

  val PLUGIN_NAME = "RaccoonMischief"

  val plugin: Plugin? by lazy {
    Bukkit.getServer().getPluginManager().getPlugin(PLUGIN_NAME)
  }

  fun namespacedKey(key: String): NamespacedKey? =
    plugin?.let { NamespacedKey(it, key) }

}
