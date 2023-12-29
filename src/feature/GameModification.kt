
package com.mercerenies.turtletroll.feature

import org.bukkit.plugin.Plugin

// Actions that run when the plugin is enabled or disabled as a whole,
// such as recipe adders and deleters.
interface GameModification {
  fun onPluginEnable(plugin: Plugin): Unit
  fun onPluginDisable(plugin: Plugin): Unit
}
