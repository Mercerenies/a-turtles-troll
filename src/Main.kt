
package com.mercerenies.turtletroll

import com.mercerenies.turtletroll.feature.FeatureManager
import com.mercerenies.turtletroll.feature.CompositeFeature
import com.mercerenies.turtletroll.recipe.StoneRecipeDeleter
import com.mercerenies.turtletroll.recipe.AnvilRecipeFeature
import com.mercerenies.turtletroll.recipe.AngelRecipeFeature
import com.mercerenies.turtletroll.recipe.DripstoneRecipeFeature

import org.bukkit.Bukkit
import org.bukkit.plugin.java.JavaPlugin

class Main : JavaPlugin() {
  val recipeDeleter = StoneRecipeDeleter(Bukkit.getServer())

  val anvilRecipeFeature = AnvilRecipeFeature(this)
  val angelRecipeFeature = AngelRecipeFeature(this)
  val dripstoneRecipeFeature = DripstoneRecipeFeature(this)

  val mainContainer = MainContainer(this)

  val anvilFeature = CompositeFeature(
    mainContainer.anvilRunnable.name,
    mainContainer.anvilRunnable.description,
    listOf(mainContainer.anvilRunnable, anvilRecipeFeature),
  )

  val angelFeature = CompositeFeature(
    mainContainer.angelManager.name,
    mainContainer.angelManager.description,
    listOf(mainContainer.angelManager, angelRecipeFeature),
  )

  val dripstoneFeature = CompositeFeature(
    mainContainer.dripstoneManager.name,
    mainContainer.dripstoneManager.description,
    listOf(mainContainer.dripstoneManager, dripstoneRecipeFeature),
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
    anvilRecipeFeature.addRecipes()
    angelRecipeFeature.addRecipes()
    dripstoneRecipeFeature.addRecipes()
    mainContainer.explosiveArrowManager.addRecipes()

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
    anvilRecipeFeature.removeRecipes()
    angelRecipeFeature.removeRecipes()
    dripstoneRecipeFeature.removeRecipes()
    mainContainer.explosiveArrowManager.removeRecipes()

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
