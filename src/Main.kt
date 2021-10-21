
package com.mercerenies.turtletroll

import com.mercerenies.turtletroll.falling.AnvilRunnable
import com.mercerenies.turtletroll.falling.SandAttackRunnable
import com.mercerenies.turtletroll.feature.FeatureManager
import com.mercerenies.turtletroll.feature.CompositeFeature
import com.mercerenies.turtletroll.recipe.StoneRecipeDeleter
import com.mercerenies.turtletroll.recipe.AnvilRecipeFeature
import com.mercerenies.turtletroll.recipe.AngelRecipeFeature
import com.mercerenies.turtletroll.recipe.DripstoneRecipeFeature

import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.plugin.java.JavaPlugin

class Main : JavaPlugin() {
  val recipeDeleter = StoneRecipeDeleter(Bukkit.getServer())
  val anvilRunnable = AnvilRunnable()
  val ghastBurnRunnable = GhastBurnRunnable(this)
  val sandAttackRunnable = SandAttackRunnable(SAND_ATTACK_TRIGGERS)

  val anvilRecipeFeature = AnvilRecipeFeature(this)
  val angelRecipeFeature = AngelRecipeFeature(this)
  val dripstoneRecipeFeature = DripstoneRecipeFeature(this)

  val listenerManager = AllPluginListeners(this)

  val anvilFeature = CompositeFeature(
    anvilRunnable.name,
    anvilRunnable.description,
    listOf(anvilRunnable, anvilRecipeFeature),
  )

  val angelFeature = CompositeFeature(
    listenerManager.angelManager.name,
    listenerManager.angelManager.description,
    listOf(listenerManager.angelManager, angelRecipeFeature),
  )

  val dripstoneFeature = CompositeFeature(
    listenerManager.dripstoneManager.name,
    listenerManager.dripstoneManager.description,
    listOf(listenerManager.dripstoneManager, dripstoneRecipeFeature),
  )

  val featureManager = FeatureManager(
    listOf(recipeDeleter, anvilFeature, angelFeature, dripstoneFeature, sandAttackRunnable, ghastBurnRunnable) + listenerManager.getFeatures()
  )

  companion object {

    val SAND_ATTACK_TRIGGERS = setOf(
      Material.SAND, Material.GRAVEL, Material.END_STONE,
    )

  }

  override fun onEnable() {
    for (listener in listenerManager) {
      Bukkit.getPluginManager().registerEvents(listener, this)
    }
    recipeDeleter.removeRecipes()
    anvilRecipeFeature.addRecipes()
    angelRecipeFeature.addRecipes()
    dripstoneRecipeFeature.addRecipes()
    listenerManager.explosiveArrowManager.addRecipes()
    anvilRunnable.register(this)
    sandAttackRunnable.register(this)
    ghastBurnRunnable.register()
    listenerManager.pufferfishRainManager.register()
    listenerManager.angelManager.register()
    listenerManager.phantomManager.register()
    listenerManager.pumpkinManager.register(this)
    listenerManager.mossManager.register(this)
    listenerManager.dripstoneManager.register()
    listenerManager.dragonBombManager.register()
    listenerManager.classicLavaManager.register()
    listenerManager.bedtimeManager.register()
    this.getCommand("turtle")!!.setExecutor(featureManager)
    this.getCommand("turtle")!!.setTabCompleter(featureManager)
  }

  override fun onDisable() {
    recipeDeleter.addRecipes()
    anvilRecipeFeature.removeRecipes()
    angelRecipeFeature.removeRecipes()
    dripstoneRecipeFeature.removeRecipes()
    listenerManager.explosiveArrowManager.removeRecipes()
    try {
      anvilRunnable.cancel()
    } catch (_: IllegalStateException) {}
    try {
      sandAttackRunnable.cancel()
    } catch (_: IllegalStateException) {}
    try {
      ghastBurnRunnable.cancel()
    } catch (_: IllegalStateException) {}
    try {
      listenerManager.pufferfishRainManager.cancel()
    } catch (_: IllegalStateException) {}
    try {
      listenerManager.angelManager.cancel()
    } catch (_: IllegalStateException) {}
    try {
      listenerManager.phantomManager.cancel()
    } catch (_: IllegalStateException) {}
    try {
      listenerManager.pumpkinManager.cancel()
    } catch (_: IllegalStateException) {}
    try {
      listenerManager.mossManager.cancel()
    } catch (_: IllegalStateException) {}
    try {
      listenerManager.dripstoneManager.cancel()
    } catch (_: IllegalStateException) {}
    try {
      listenerManager.dragonBombManager.cancel()
    } catch (_: IllegalStateException) {}
    try {
      listenerManager.classicLavaManager.cancel()
    } catch (_: IllegalStateException) {}
    try {
      listenerManager.bedtimeManager.cancel()
    } catch (_: IllegalStateException) {}
    this.getCommand("turtle")?.setExecutor(null)
    this.getCommand("turtle")?.setTabCompleter(null)
  }

}
