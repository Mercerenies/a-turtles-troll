
package com.mercerenies.turtletroll

import com.mercerenies.turtletroll.feature.FeatureManager
import com.mercerenies.turtletroll.feature.CompositeFeature
import com.mercerenies.turtletroll.recipe.StoneRecipeDeleter

import org.bukkit.Bukkit
import org.bukkit.plugin.java.JavaPlugin

class Main : JavaPlugin() {
  val recipeDeleter = StoneRecipeDeleter(Bukkit.getServer())

  val mainContainer = MainContainer(this)

  val anvilFeature = CompositeFeature(
    mainContainer.anvilRunnable.name,
    mainContainer.anvilRunnable.description,
    listOf(mainContainer.anvilRunnable, mainContainer.anvilRecipeFeature),
  )

  val angelFeature = CompositeFeature(
    mainContainer.angelManager.name,
    mainContainer.angelManager.description,
    listOf(mainContainer.angelManager, mainContainer.angelRecipeFeature),
  )

  val dripstoneFeature = CompositeFeature(
    mainContainer.dripstoneManager.name,
    mainContainer.dripstoneManager.description,
    listOf(mainContainer.dripstoneManager, mainContainer.dripstoneRecipeFeature),
  )

  val featureManager = FeatureManager(
    listOf(recipeDeleter, anvilFeature, angelFeature, dripstoneFeature) + mainContainer.features
  )

  override fun onEnable() {

    // Initialize all listeners
    for (listener in mainContainer.listeners) {
      Bukkit.getPluginManager().registerEvents(listener, this)
    }

    // Recipe modifications
    recipeDeleter.removeRecipes()
    for (recipe in mainContainer.recipes) {
      recipe.addRecipes()
    }

    // Runnables
    for (runnable in mainContainer.runnables) {
      runnable.register()
    }

    // Setup command
    this.getCommand("turtle")!!.setExecutor(featureManager)
    this.getCommand("turtle")!!.setTabCompleter(featureManager)

  }

  override fun onDisable() {

    // Recipe modifications
    recipeDeleter.addRecipes()
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
