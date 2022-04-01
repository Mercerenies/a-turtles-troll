
package com.mercerenies.turtletroll

import com.mercerenies.turtletroll.feature.FeatureManager
import com.mercerenies.turtletroll.command.CommandDispatcher
import com.mercerenies.turtletroll.command.Subcommand
import com.mercerenies.turtletroll.command.withPermission

import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.Bukkit

class Main : JavaPlugin() {
  val mainContainer = MainContainer(this)
  val featureManager = FeatureManager(mainContainer.features)
  val turtleCommand = Subcommand(
    "turtle" to Subcommand(
      "on" to featureManager.OnCommand.withPermission("com.mercerenies.turtletroll.feature.toggle"),
      "off" to featureManager.OffCommand.withPermission("com.mercerenies.turtletroll.feature.toggle"),
      "list" to featureManager.ListCommand.withPermission("com.mercerenies.turtletroll.feature.list"),
      "bedtime" to mainContainer.bedtimeManager.BedtimeCommand.withPermission("com.mercerenies.turtletroll.command.bedtime"),
    ).withPermission("com.mercerenies.turtletroll.command"),
  )
  val commandDispatcher = CommandDispatcher(turtleCommand)

  override fun onEnable() {

    // Initialize all listeners
    for (listener in mainContainer.listeners) {
      Bukkit.getPluginManager().registerEvents(listener, this)
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

  }

  override fun onDisable() {

    // Recipe modifications
    for (deleter in mainContainer.recipeDeleters) {
      deleter.addRecipes()
    }
    for (recipe in mainContainer.recipes) {
      recipe.removeRecipes()
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

  }

}
