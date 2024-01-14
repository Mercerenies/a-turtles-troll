
package com.mercerenies.turtletroll.bridge

import github.scarsz.discordsrv.DiscordSRV
import github.scarsz.discordsrv.util.DiscordUtil

object DiscordSRVBridge : PluginBridge() {

  private val GLOBAL_CHANNEL_KEY = "global"

  override val pluginName = "DiscordSRV"

  fun broadcastMessageIfAvailable(message: String) {
    val thisPlugin = plugin
    if (thisPlugin == null) {
      return
    }

    val textChannel = (thisPlugin as DiscordSRV).getOptionalTextChannel(GLOBAL_CHANNEL_KEY)
    if (textChannel == null) {
      // Could not find channel; abort
      return
    }

    DiscordUtil.sendMessage(textChannel, message)
  }

}
