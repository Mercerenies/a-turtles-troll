
package com.mercerenies.turtletroll

import com.mercerenies.turtletroll.feature.FeatureManager
import com.mercerenies.turtletroll.feature.FeatureConfig
import com.mercerenies.turtletroll.feature.container.FeatureContainer
import com.mercerenies.turtletroll.feature.builder.SimpleBuilderState
import com.mercerenies.turtletroll.feature.builder.BuilderState
import com.mercerenies.turtletroll.command.CommandDispatcher
import com.mercerenies.turtletroll.command.Subcommand
import com.mercerenies.turtletroll.command.withPermission
import com.mercerenies.turtletroll.command.Permissions
import com.mercerenies.turtletroll.storage.GlobalDataHolder
import com.mercerenies.turtletroll.storage.GlobalFileDataHolder
import com.mercerenies.turtletroll.config.BukkitConfigOptions
import com.mercerenies.turtletroll.config.CheckedConfigOptions
import com.mercerenies.turtletroll.config.DifficultyBackedConfigOptions

import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.Bukkit

import com.comphenix.protocol.ProtocolLibrary

class TurtleTrollPlugin : JavaPlugin() {

  private val dataHolder = GlobalFileDataHolder(this)

  // Expose dataHolder but only as a GlobalDataHolder. The
  // implementation is private.
  val pluginData: GlobalDataHolder
    get() = dataHolder

  val configOptions: CheckedConfigOptions
    get() {
      val bukkitConfig = BukkitConfigOptions(this.getConfig())
      return CheckedConfigOptions(DifficultyBackedConfigOptions(bukkitConfig))
    }

  val mainContainer: FeatureContainer =
    AllFeatureFactories.createComposite(this.createBuilderState())
  val featureManager = FeatureManager(mainContainer.features)
  val turtleCommand = Subcommand(
    "turtle" to Subcommand(
      "on" to featureManager.OnCommand.withPermission(Permissions.FEATURE_TOGGLE),
      "off" to featureManager.OffCommand.withPermission(Permissions.FEATURE_TOGGLE),
      "list" to featureManager.ListCommand.withPermission(Permissions.FEATURE_LIST),
      "describe" to featureManager.DescribeCommand.withPermission(Permissions.FEATURE_DESCRIBE),
      "dbg" to Subcommand(
        mainContainer.debugCommands.toMap().mapValues { it.value.withPermission(Permissions.DEBUG) }
      ).withPermission(Permissions.DEBUG),
      *mainContainer.commands.toList().toTypedArray(),
    ).withPermission(Permissions.BASE_COMMAND),
  )
  val commandDispatcher = CommandDispatcher(turtleCommand)

  override fun onLoad() {
    this.saveDefaultConfig()
  }

  override fun onEnable() {

    // Reload the data file
    dataHolder.reload()

    // Initialize all listeners
    val pluginManager = Bukkit.getPluginManager()
    for (listener in mainContainer.listeners) {
      pluginManager.registerEvents(listener, this)
    }

    // Initialize all packet listeners
    val protocolManager = ProtocolLibrary.getProtocolManager()
    for (listener in mainContainer.packetListeners) {
      protocolManager.addPacketListener(listener)
    }

    // Recipe modifications
    for (deleter in mainContainer.recipeDeleters) {
      deleter.removeRecipes()
    }
    for (recipe in mainContainer.recipes) {
      recipe.addRecipes()
    }

    // Runnables
    for (runnable in mainContainer.runnables) {
      runnable.register()
    }

    // Setup command
    this.getCommand("turtle")!!.setExecutor(commandDispatcher)
    this.getCommand("turtle")!!.setTabCompleter(commandDispatcher)

    // Disable features as requested by config
    disableFeaturesFromConfig()

  }

  override fun onDisable() {

    // Recipe modifications
    for (deleter in mainContainer.recipeDeleters) {
      deleter.addRecipes()
    }
    for (recipe in mainContainer.recipes) {
      recipe.removeRecipes()
    }

    // Remove all packet listeners
    val protocolManager = ProtocolLibrary.getProtocolManager()
    for (listener in mainContainer.packetListeners) {
      protocolManager.removePacketListener(listener)
    }

    // Runnables
    for (runnable in mainContainer.runnables) {
      try {
        runnable.cancel()
      } catch (_: IllegalStateException) {}
    }

    // Remove command
    this.getCommand("turtle")?.setExecutor(null)
    this.getCommand("turtle")?.setTabCompleter(null)

    // Save the data file
    dataHolder.save()

  }

  fun createBuilderState(): BuilderState =
    SimpleBuilderState(this, this.pluginData, this.configOptions)

  private fun disableFeaturesFromConfig() {
    val config = this.configOptions
    for (feature in featureManager.features) {
      val enabled = config.getBoolean(FeatureConfig.featureEnabledPath(feature))
      if (!enabled) {
        feature.disable()
      }
    }
  }

}
