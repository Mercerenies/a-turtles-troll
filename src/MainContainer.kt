
package com.mercerenies.turtletroll

import com.mercerenies.turtletroll.falling.AnvilRunnable
import com.mercerenies.turtletroll.cake.CakeListener
import com.mercerenies.turtletroll.cake.CakeEat
import com.mercerenies.turtletroll.egg.EggListener
import com.mercerenies.turtletroll.egg.EggHatch
import com.mercerenies.turtletroll.dripstone.DripstoneManager
import com.mercerenies.turtletroll.gravestone.BedtimeManager
import com.mercerenies.turtletroll.feature.container.FeatureContainer
import com.mercerenies.turtletroll.feature.Feature
import com.mercerenies.turtletroll.feature.RunnableFeature
import com.mercerenies.turtletroll.feature.CompositeFeature
import com.mercerenies.turtletroll.recipe.RecipeFeature
import com.mercerenies.turtletroll.recipe.AnvilRecipeFeature
import com.mercerenies.turtletroll.recipe.DripstoneRecipeFeature
import com.mercerenies.turtletroll.recipe.RecipeDeleter
import com.mercerenies.turtletroll.recipe.MelonRecipeDeleter
import com.mercerenies.turtletroll.recipe.MelonRecipeFeature
import com.mercerenies.turtletroll.command.Command
import com.mercerenies.turtletroll.command.PermittedCommand
import com.mercerenies.turtletroll.command.withPermission

import org.bukkit.plugin.Plugin
import org.bukkit.event.Listener
import org.bukkit.Bukkit

// My hope is to deprecate and remove this class, replacing it with
// more focused FeatureContainer instances.
class MainContainer(val plugin: Main) : FeatureContainer {

  val pumpkinManager = PumpkinSlownessManager(plugin) // <- things depend on it
  val explosiveArrowManager = ExplosiveArrowManager(plugin) // <- also a recipe
  val dripstoneManager = DripstoneManager(plugin) // <- also a recipe
  val classicLavaManager = ClassicLavaManager(plugin) // <- things depend on it
  val bedtimeManager = BedtimeManager(plugin) // <- contains a command

  val breakEvents = BlockBreakEvents() // <- oh god
  val electricListener = ElectricWaterListener(plugin, pumpkinManager) // <- depends on pumpkin mgr
  val lightListener = BreakLightOnSightListener(plugin, pumpkinManager) // <- depends on pumpkin
  val cakeListener = CakeListener(plugin, CakeEat.defaultEffects(plugin)) // <- takes args
  val bedListener = BedDropListener() // <- composite feature part with block break
  val eggListener = EggListener(EggHatch.defaultEffects(plugin)) // <- takes args
  val overgrowthListener = OvergrowthListener(plugin, OvergrowthListener::randomWood) // <- takes args
  val ghastLavaListener = GhastLavaListener(plugin, classicLavaManager.ignorer) // <- depends on classic lava
  val carvePumpkinListener = CarvePumpkinListener() // <- composite

  val anvilRunnable = AnvilRunnable(plugin) // <- composite

  val anvilRecipeFeature = AnvilRecipeFeature(plugin) // <- composite
  val dripstoneRecipeFeature = DripstoneRecipeFeature(plugin) // <- composite
  val melonRecipeFeature = MelonRecipeFeature(plugin) // <- composite

  val melonRecipeDeleter = MelonRecipeDeleter(Bukkit.getServer()) // <- composite

  val anvilFeature = CompositeFeature(
    anvilRunnable.name,
    anvilRunnable.description,
    listOf(anvilRunnable, anvilRecipeFeature),
  )

  val dripstoneFeature = CompositeFeature(
    dripstoneManager.name,
    dripstoneManager.description,
    listOf(dripstoneManager, dripstoneRecipeFeature),
  )

  val melompkinFeature = CompositeFeature(
    "melompkin",
    "Several features of melons and pumpkins are interchanged",
    listOf(melonRecipeFeature, melonRecipeDeleter, carvePumpkinListener, breakEvents.melonPumpkinsFeature),
  )

  // CancelDropAction is a BlockBreakAction and BedDropListener is a
  // Bukkit event listener, but conceptually they do the same thing,
  // so we want to treat them as one feature.
  private val dropCompositeFeature =
    CompositeFeature(
      breakEvents.cancelDropAction.name,
      breakEvents.cancelDropAction.description,
      listOf(breakEvents.cancelDropAction, bedListener)
    )

  override val listeners: List<Listener> =
    listOf(
      breakEvents.listener, electricListener, pumpkinManager,
      bedListener, eggListener, explosiveArrowManager,
      cakeListener, dripstoneManager, overgrowthListener, classicLavaManager,
      bedtimeManager, carvePumpkinListener, ghastLavaListener,
    )

  override val features: List<Feature> =
    listOf(
      lightListener, pumpkinManager, dropCompositeFeature, eggListener,
      explosiveArrowManager, cakeListener, overgrowthListener, classicLavaManager,
      bedtimeManager, anvilFeature, dripstoneFeature,
      melompkinFeature, ghastLavaListener,
    ) + (breakEvents.getFeatures() - breakEvents.cancelDropAction)

  override val runnables: List<RunnableFeature> =
    listOf(
      anvilRunnable, pumpkinManager,
      dripstoneManager, classicLavaManager, bedtimeManager,
    )

  override val recipes: List<RecipeFeature> =
    listOf(
      anvilRecipeFeature, dripstoneRecipeFeature, explosiveArrowManager, melonRecipeFeature,
    )

  override val recipeDeleters: List<RecipeDeleter> =
    listOf(
      melonRecipeDeleter,
    )

  override val commands: List<Pair<String, PermittedCommand<Command>>> =
    listOf(
      "bedtime" to bedtimeManager.BedtimeCommand.withPermission("com.mercerenies.turtletroll.command.bedtime"),
    )

}
