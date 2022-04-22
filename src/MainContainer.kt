
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
import com.mercerenies.turtletroll.gravestone.DeathScoreboardListener
import com.mercerenies.turtletroll.gravestone.BedtimeManager
import com.mercerenies.turtletroll.feature.container.FeatureContainer
import com.mercerenies.turtletroll.feature.Feature
import com.mercerenies.turtletroll.feature.RunnableFeature
import com.mercerenies.turtletroll.feature.CompositeFeature
import com.mercerenies.turtletroll.recipe.RecipeFeature
import com.mercerenies.turtletroll.recipe.AnvilRecipeFeature
import com.mercerenies.turtletroll.recipe.AngelRecipeFeature
import com.mercerenies.turtletroll.recipe.DripstoneRecipeFeature
import com.mercerenies.turtletroll.recipe.DirtRecipeFeature
import com.mercerenies.turtletroll.recipe.RecipeDeleter
import com.mercerenies.turtletroll.recipe.StoneRecipeDeleter
import com.mercerenies.turtletroll.recipe.MelonRecipeDeleter
import com.mercerenies.turtletroll.recipe.MelonRecipeFeature
import com.mercerenies.turtletroll.cookie.FreeCookieRunnable
import com.mercerenies.turtletroll.spillage.SpillageListener
import com.mercerenies.turtletroll.command.Command
import com.mercerenies.turtletroll.command.PermittedCommand
import com.mercerenies.turtletroll.command.withPermission

import org.bukkit.plugin.Plugin
import org.bukkit.event.Listener
import org.bukkit.Bukkit

// My hope is to deprecate and remove this class, replacing it with
// more focused FeatureContainer instances.
class MainContainer(val plugin: Main) : FeatureContainer {

  val pumpkinManager = PumpkinSlownessManager(plugin)
  val angelManager = WeepingAngelManager(plugin)
  val explosiveArrowManager = ExplosiveArrowManager(plugin)
  val dripstoneManager = DripstoneManager(plugin)
  val classicLavaManager = ClassicLavaManager(plugin)
  val bedtimeManager = BedtimeManager(plugin)

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
  val ghastLavaListener = GhastLavaListener(plugin, classicLavaManager.ignorer)
  val goddessHoeListener = GoddessHoeListener(plugin)
  val oldAgeListener = OldAgeListener()
  val namedZombieListener = NamedZombieListener()
  val wanderingTraderListener = WanderingTraderListener()
  val zombieSpeedListener = ZombieSpeedListener()
  val shieldSurfListener = ShieldSurfListener()
  val witherBowListener = WitherBowListener()
  val catBatListener = CatBatListener(plugin)
  val bambooSpreadListener = BambooSpreadListener(plugin)
  val zombieDrowningListener = ZombieDrowningListener()
  val carvePumpkinListener = CarvePumpkinListener()
  val escalationListener = EscalationListener()
  val butterfingersListener = ButterfingersListener()
  val spillageListener = SpillageListener(plugin)
  val eggshellsListener = EggshellsListener()
  val deathScoreboardListener = DeathScoreboardListener(plugin, plugin.pluginData)

  val anvilRunnable = AnvilRunnable(plugin)
  val ghastBurnRunnable = GhastBurnRunnable(plugin)
  val sandAttackRunnable = SandAttackRunnable(plugin)
  val freeCookieRunnable = FreeCookieRunnable(plugin)
  val silverfishBurnRunnable = SilverfishBurnRunnable(plugin)

  val anvilRecipeFeature = AnvilRecipeFeature(plugin)
  val angelRecipeFeature = AngelRecipeFeature(plugin)
  val dripstoneRecipeFeature = DripstoneRecipeFeature(plugin)
  val dirtRecipeFeature = DirtRecipeFeature(plugin)
  val melonRecipeFeature = MelonRecipeFeature(plugin)

  val stoneRecipeDeleter = StoneRecipeDeleter(Bukkit.getServer())
  val melonRecipeDeleter = MelonRecipeDeleter(Bukkit.getServer())

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
      breakEvents.listener, chickenListener, grassListener, snowListener,
      ghastListener, ravagerListener, skeleListener, electricListener,
      blazeListener, zombifyListener, forestFireListener, roseListener,
      endStoneListener, doorListener, angelManager, levitationListener,
      buttonListener, plateListener, slabListener, lightListener,
      lavaListener, pumpkinManager, mimicListener,
      bedListener, eggListener, eggArrowListener, eggDropListener,
      witherArmorListener, explosiveArrowManager,
      cakeListener, dripstoneManager, glassLuckListener, endDirtListener,
      overgrowthListener, endCrystalListener,
      pillagerGunListener, classicLavaManager, fallDamageListener, chargedCreeperListener,
      drownedListener, gravestoneListener, axolotlListener, bedtimeManager,
      goddessHoeListener, oldAgeListener, namedZombieListener,
      wanderingTraderListener, zombieSpeedListener,
      shieldSurfListener, witherBowListener, catBatListener,
      bambooSpreadListener, zombieDrowningListener, carvePumpkinListener,
      escalationListener, butterfingersListener, spillageListener, eggshellsListener,
      ghastLavaListener, deathScoreboardListener,
    )

  override val features: List<Feature> =
    listOf(
      chickenListener, grassListener, snowListener,
      ghastListener, ravagerListener, skeleListener,
      blazeListener, zombifyListener, forestFireListener,
      roseListener, endStoneListener, doorListener,
      levitationListener, buttonListener,
      plateListener, slabListener, lightListener,
      lavaListener, pumpkinManager,
      mimicListener, dropCompositeFeature, eggListener,
      eggArrowListener, eggDropListener, witherArmorListener,
      explosiveArrowManager, cakeListener,
      glassLuckListener, endDirtListener,
      overgrowthListener, endCrystalListener,
      pillagerGunListener, classicLavaManager,
      fallDamageListener, chargedCreeperListener,
      drownedListener, gravestoneListener, axolotlListener,
      bedtimeManager, sandAttackRunnable,
      ghastBurnRunnable, anvilFeature, angelFeature, dripstoneFeature,
      stoneRecipeDeleter, goddessHoeListener, oldAgeListener, namedZombieListener,
      wanderingTraderListener, zombieSpeedListener,
      shieldSurfListener, freeCookieRunnable, dirtRecipeFeature,
      witherBowListener, catBatListener, bambooSpreadListener, zombieDrowningListener,
      melompkinFeature, escalationListener, butterfingersListener, spillageListener,
      eggshellsListener, ghastLavaListener, deathScoreboardListener, silverfishBurnRunnable,
    ) + (breakEvents.getFeatures() - breakEvents.cancelDropAction)

  override val runnables: List<RunnableFeature> =
    listOf(
      anvilRunnable, sandAttackRunnable, ghastBurnRunnable,
      angelManager, pumpkinManager,
      dripstoneManager, classicLavaManager, bedtimeManager,
      freeCookieRunnable, silverfishBurnRunnable,
    )

  override val recipes: List<RecipeFeature> =
    listOf(
      anvilRecipeFeature, angelRecipeFeature, dripstoneRecipeFeature, explosiveArrowManager,
      dirtRecipeFeature, melonRecipeFeature,
    )

  override val recipeDeleters: List<RecipeDeleter> =
    listOf(
      stoneRecipeDeleter, melonRecipeDeleter,
    )

  override val commands: List<Pair<String, PermittedCommand<Command>>> =
    listOf(
      "bedtime" to bedtimeManager.BedtimeCommand.withPermission("com.mercerenies.turtletroll.command.bedtime"),
    )

}
