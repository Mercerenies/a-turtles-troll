
package com.mercerenies.turtletroll

import com.mercerenies.turtletroll.feature.FeatureManager
import com.mercerenies.turtletroll.command.TurtleCommand
import com.mercerenies.turtletroll.command.CommandDispatcher

import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.Bukkit

class Main : JavaPlugin() {
  val mainContainer = MainContainer(this)
  val featureManager = FeatureManager(mainContainer.features)
  val turtleCommand = TurtleCommand(featureManager)
  val commandDispatcher = CommandDispatcher(turtleCommand)

  override fun onEnable() {

    // Initialize all listeners
    for (listener in mainContainer.listeners) {
      Bukkit.getPluginManager().registerEvents(listener, this)
    }

    // Recipe modifications
    mainContainer.recipeDeleter.removeRecipes()
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
    mainContainer.recipeDeleter.addRecipes()
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
