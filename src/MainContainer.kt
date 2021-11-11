
package com.mercerenies.turtletroll

import com.mercerenies.turtletroll.falling.AnvilRunnable
import com.mercerenies.turtletroll.falling.SandAttackRunnable
import com.mercerenies.turtletroll.chicken.ChickenDamageListener
import com.mercerenies.turtletroll.transformed.GhastSpawnerListener
import com.mercerenies.turtletroll.transformed.RavagerSpawnerListener
import com.mercerenies.turtletroll.transformed.DrownedSpawnerListener
import com.mercerenies.turtletroll.durability.DoorDamageListener
import com.mercerenies.turtletroll.durability.ButtonDamageListener
import com.mercerenies.turtletroll.angel.WeepingAngelManager
import com.mercerenies.turtletroll.mimic.MimicListener
import com.mercerenies.turtletroll.cake.CakeListener
import com.mercerenies.turtletroll.cake.CakeEat
import com.mercerenies.turtletroll.egg.EggListener
import com.mercerenies.turtletroll.egg.EggHatch
import com.mercerenies.turtletroll.dripstone.DripstoneManager
import com.mercerenies.turtletroll.gravestone.GravestoneListener
import com.mercerenies.turtletroll.gravestone.BedtimeManager
import com.mercerenies.turtletroll.feature.Feature
import com.mercerenies.turtletroll.feature.RunnableFeature
import com.mercerenies.turtletroll.feature.CompositeFeature
import com.mercerenies.turtletroll.recipe.RecipeFeature
import com.mercerenies.turtletroll.recipe.AnvilRecipeFeature
import com.mercerenies.turtletroll.recipe.AngelRecipeFeature
import com.mercerenies.turtletroll.recipe.DripstoneRecipeFeature
import com.mercerenies.turtletroll.recipe.DirtRecipeFeature
import com.mercerenies.turtletroll.recipe.StoneRecipeDeleter
import com.mercerenies.turtletroll.cookie.FreeCookieRunnable

import org.bukkit.plugin.Plugin
import org.bukkit.event.Listener
import org.bukkit.Bukkit

class MainContainer(val plugin: Plugin) {

  val pumpkinManager = PumpkinSlownessManager(plugin)
  val angelManager = WeepingAngelManager(plugin)
  val phantomManager = PetPhantomManager(plugin)
  val mossManager = ContagiousMossManager(plugin)
  val explosiveArrowManager = ExplosiveArrowManager(plugin)
  val dripstoneManager = DripstoneManager(plugin)
  val dragonBombManager = DragonBombManager(plugin)
  val pufferfishRainManager = PufferfishRainManager(plugin)
  val classicLavaManager = ClassicLavaManager(plugin)
  val bedtimeManager = BedtimeManager(plugin)
  val llamaHunterManager = LlamaHunterManager(plugin)

  val breakEvents = BlockBreakEvents()
  val chickenListener = ChickenDamageListener(plugin)
  val grassListener = GrassPoisonListener()
  val snowListener = SnowListener()
  val endStoneListener = EndStoneListener()
  val ghastListener = GhastSpawnerListener(plugin)
  val ravagerListener = RavagerSpawnerListener(plugin)
  val skeleListener = SkeletonWitherListener()
  val electricListener = ElectricWaterListener(plugin, pumpkinManager)
  val blazeListener = BlazeAttackListener(plugin)
  val zombifyListener = ZombifyTradeListener()
  val forestFireListener = ForestFireListener(plugin)
  val roseListener = WitherRoseListener()
  val doorListener = DoorDamageListener()
  val buttonListener = ButtonDamageListener()
  val levitationListener = LevitationListener()
  val plateListener = PressurePlateFireListener()
  val slabListener = SlowSlabListener()
  val lightListener = BreakLightOnSightListener(plugin, pumpkinManager)
  val lavaListener = LavaLaunchListener()
  val mimicListener = MimicListener(plugin)
  val cakeListener = CakeListener(plugin, CakeEat.defaultEffects(plugin))
  val bedListener = BedDropListener()
  val eggListener = EggListener(EggHatch.defaultEffects(plugin))
  val eggArrowListener = EggArrowListener()
  val eggDropListener = EggDropListener()
  val witherArmorListener = WitherArmorListener()
  val glassLuckListener = GlassLuckListener()
  val endDirtListener = EndDirtListener()
  val overgrowthListener = OvergrowthListener(plugin, OvergrowthListener::randomWood)
  val endCrystalListener = EndCrystalListener(plugin)
  val pillagerGunListener = PillagerGunListener()
  val fallDamageListener = FallDamageListener()
  val chargedCreeperListener = ChargedCreeperListener()
  val drownedListener = DrownedSpawnerListener(plugin)
  val gravestoneListener = GravestoneListener(plugin)
  val axolotlListener = AxolotlListener()
  //val ghastLavaListener = GhastLavaListener(plugin, classicLavaManager.ignorer)
  val goddessHoeListener = GoddessHoeListener(plugin)
  val oldAgeListener = OldAgeListener()
  val namedZombieListener = NamedZombieListener()
  val wanderingTraderListener = WanderingTraderListener()
  val zombieSpeedListener = ZombieSpeedListener()
  val shieldSurfListener = ShieldSurfListener()

  val anvilRunnable = AnvilRunnable(plugin)
  val ghastBurnRunnable = GhastBurnRunnable(plugin)
  val sandAttackRunnable = SandAttackRunnable(plugin)
  val freeCookieRunnable = FreeCookieRunnable(plugin)

  val anvilRecipeFeature = AnvilRecipeFeature(plugin)
  val angelRecipeFeature = AngelRecipeFeature(plugin)
  val dripstoneRecipeFeature = DripstoneRecipeFeature(plugin)
  val dirtRecipeFeature = DirtRecipeFeature(plugin)

  val recipeDeleter = StoneRecipeDeleter(Bukkit.getServer())

  val anvilFeature = CompositeFeature(
    anvilRunnable.name,
    anvilRunnable.description,
    listOf(anvilRunnable, anvilRecipeFeature),
  )

  val angelFeature = CompositeFeature(
    angelManager.name,
    angelManager.description,
    listOf(angelManager, angelRecipeFeature),
  )

  val dripstoneFeature = CompositeFeature(
    dripstoneManager.name,
    dripstoneManager.description,
    listOf(dripstoneManager, dripstoneRecipeFeature),
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

  val listeners: List<Listener> =
    listOf(
      breakEvents.listener, chickenListener, grassListener, snowListener,
      ghastListener, ravagerListener, skeleListener, electricListener,
      blazeListener, zombifyListener, forestFireListener, roseListener,
      endStoneListener, doorListener, angelManager, levitationListener,
      buttonListener, plateListener, slabListener, lightListener,
      phantomManager, lavaListener, pumpkinManager, mimicListener,
      bedListener, eggListener, eggArrowListener, eggDropListener,
      witherArmorListener, mossManager, explosiveArrowManager,
      cakeListener, dripstoneManager, glassLuckListener, endDirtListener,
      overgrowthListener, endCrystalListener, dragonBombManager, pufferfishRainManager,
      pillagerGunListener, classicLavaManager, fallDamageListener, chargedCreeperListener,
      drownedListener, gravestoneListener, axolotlListener, bedtimeManager,
      goddessHoeListener, oldAgeListener, namedZombieListener,
      wanderingTraderListener, zombieSpeedListener, llamaHunterManager,
      shieldSurfListener,
    )

  val features: List<Feature> =
    listOf(
      chickenListener, grassListener, snowListener,
      ghastListener, ravagerListener, skeleListener,
      blazeListener, zombifyListener, forestFireListener,
      roseListener, endStoneListener, doorListener,
      levitationListener, buttonListener,
      plateListener, slabListener, lightListener,
      phantomManager, lavaListener, pumpkinManager,
      mimicListener, dropCompositeFeature, eggListener,
      eggArrowListener, eggDropListener, witherArmorListener,
      mossManager, explosiveArrowManager, cakeListener,
      glassLuckListener, endDirtListener,
      overgrowthListener, endCrystalListener,
      dragonBombManager, pufferfishRainManager,
      pillagerGunListener, classicLavaManager,
      fallDamageListener, chargedCreeperListener,
      drownedListener, gravestoneListener, axolotlListener,
      bedtimeManager, sandAttackRunnable,
      ghastBurnRunnable, anvilFeature, angelFeature, dripstoneFeature,
      recipeDeleter, goddessHoeListener, oldAgeListener, namedZombieListener,
      wanderingTraderListener, zombieSpeedListener, llamaHunterManager,
      shieldSurfListener, freeCookieRunnable,
    ) + (breakEvents.getFeatures() - breakEvents.cancelDropAction)

  val runnables: List<RunnableFeature> =
    listOf(
      anvilRunnable, sandAttackRunnable, ghastBurnRunnable,
      pufferfishRainManager, angelManager, phantomManager, pumpkinManager, mossManager,
      dripstoneManager, dragonBombManager, classicLavaManager, bedtimeManager, llamaHunterManager,
      freeCookieRunnable,
    )

  val recipes: List<RecipeFeature> =
    listOf(
      anvilRecipeFeature, angelRecipeFeature, dripstoneRecipeFeature, explosiveArrowManager,
      dirtRecipeFeature,
    )

}
