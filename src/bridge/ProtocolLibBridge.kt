
package com.mercerenies.turtletroll.bridge

import com.comphenix.protocol.ProtocolLibrary
import com.comphenix.protocol.ProtocolManager

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
