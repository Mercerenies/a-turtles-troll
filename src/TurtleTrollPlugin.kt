
package com.mercerenies.turtletroll

import com.mercerenies.turtletroll.feature.FeatureConfig
import com.mercerenies.turtletroll.integration.TwitchStatistics
import com.mercerenies.turtletroll.bridge.ProtocolLibBridge

import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.Bukkit

class TurtleTrollPlugin : JavaPlugin() {

  companion object {
    val SUPPRESS_PROTOCOLLIB_WARNING = "global.suppress_protocollib_warning"
    val MIRROR_TO_DISCORDSRV = "global.mirror_to_discordsrv"
  }

  private var pluginBody: TurtleTrollPluginBody? =
    null

  override fun onLoad() {
    pluginBody = TurtleTrollPluginBody(this)
    this.saveDefaultConfig()
  }

  override fun onEnable() {
    val pluginBody = this.pluginBody!!

    // Initialize singleton integrations.
    TwitchStatistics.initializeSingleton(this)

    // Reload the data file
    pluginBody.pluginData.reload()

    // Initialize all listeners
    val pluginManager = Bukkit.getPluginManager()
    for (listener in pluginBody.mainContainer.listeners) {
      pluginManager.registerEvents(listener, this)
    }

    // Initialize all packet listeners
    val protocolManager = ProtocolLibBridge.getProtocolManager()
    if (protocolManager == null) {
      if (!pluginBody.configOptions.getBoolean(SUPPRESS_PROTOCOLLIB_WARNING)) {
        Bukkit.getLogger().warning("ProtocolLib is not installed! Some features of A Turtle's Troll will be unavailable.")
      }
    } else {
      for (listener in pluginBody.mainContainer.packetListeners) {
        protocolManager.addPacketListener(listener)
      }
    }

    // Game modifications
    for (mod in pluginBody.mainContainer.gameModifications) {
      mod.onPluginEnable(this)
    }

    // Setup command
    this.getCommand("turtle")!!.setExecutor(pluginBody.commandDispatcher)
    this.getCommand("turtle")!!.setTabCompleter(pluginBody.commandDispatcher)

    // Disable features as requested by config
    disableFeaturesFromConfig(pluginBody)

    // Configure Discord integration
    Messages.shouldMirrorToDiscord = pluginBody.configOptions.getBoolean(MIRROR_TO_DISCORDSRV)

  }

  override fun onDisable() {
    val pluginBody = this.pluginBody!!

    // Game modifications
    for (mod in pluginBody.mainContainer.gameModifications) {
      mod.onPluginDisable(this)
    }

    // Remove all packet listeners
    val protocolManager = ProtocolLibBridge.getProtocolManager()
    if (protocolManager != null) {
      for (listener in pluginBody.mainContainer.packetListeners) {
        protocolManager.removePacketListener(listener)
      }
    }

    // Remove command
    this.getCommand("turtle")?.setExecutor(null)
    this.getCommand("turtle")?.setTabCompleter(null)

    // Save the data file
    pluginBody.pluginData.save()

  }

  private fun disableFeaturesFromConfig(pluginBody: TurtleTrollPluginBody) {
    val config = pluginBody.configOptions
    for (feature in pluginBody.featureManager.features) {
      val enabled = config.getBoolean(FeatureConfig.featureEnabledPath(feature))
      if (!enabled) {
        feature.disable()
      }
    }
  }

}
