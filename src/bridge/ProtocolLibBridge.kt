
package com.mercerenies.turtletroll.bridge

import com.comphenix.protocol.ProtocolLibrary
import com.comphenix.protocol.ProtocolManager

// As of 1.23.3, we don't use ProtocolLib for anything (since
// `rainwater` is available in stock Bukkit now). But this class still
// exists, in case we need it for something ridiculous down the road.
object ProtocolLibBridge : PluginBridge() {

  override val pluginName = "ProtocolLib"

  fun getProtocolManager(): ProtocolManager? {
    if (plugin == null) {
      // Dependency does not exist; bail out.
      return null
    }
    return ProtocolLibrary.getProtocolManager()
  }

}
