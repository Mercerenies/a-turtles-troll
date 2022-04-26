
package com.mercerenies.turtletroll

import com.mercerenies.turtletroll.feature.container.FeatureContainer
import com.mercerenies.turtletroll.feature.builder.FeatureContainerFactory
import com.mercerenies.turtletroll.feature.builder.BuilderState
import com.mercerenies.turtletroll.chicken.ChickenDamageListener
import com.mercerenies.turtletroll.transformed.GhastSpawnerListener
import com.mercerenies.turtletroll.transformed.RavagerSpawnerListener
import com.mercerenies.turtletroll.transformed.DrownedSpawnerListener
import com.mercerenies.turtletroll.durability.DoorDamageListener
import com.mercerenies.turtletroll.durability.ButtonDamageListener
import com.mercerenies.turtletroll.mimic.MimicListener
import com.mercerenies.turtletroll.gravestone.GravestoneListener
import com.mercerenies.turtletroll.gravestone.DeathScoreboardListener
import com.mercerenies.turtletroll.gravestone.BedtimeManager
import com.mercerenies.turtletroll.spillage.SpillageListener
import com.mercerenies.turtletroll.falling.SandAttackRunnable
import com.mercerenies.turtletroll.cookie.FreeCookieRunnable
import com.mercerenies.turtletroll.recipe.DirtRecipeFeature
import com.mercerenies.turtletroll.recipe.StoneRecipeDeleter
import com.mercerenies.turtletroll.angel.WeepingAngelManagerFactory
import com.mercerenies.turtletroll.falling.AnvilRunnableFactory
import com.mercerenies.turtletroll.dripstone.DripstoneManagerFactory
import com.mercerenies.turtletroll.cake.CakeListenerFactory
import com.mercerenies.turtletroll.egg.EggListenerFactory
import com.mercerenies.turtletroll.overgrowth.OvergrowthListenerFactory
import com.mercerenies.turtletroll.overgrowth.OvergrowthListener
import com.mercerenies.turtletroll.ghastlava.GhastLavaListenerFactory

object AllFeatureFactories {

  val allFactories: List<FeatureContainerFactory<FeatureContainer>> =
    listOf(
      PetPhantomManager, ContagiousMossManager, DragonBombManager, PufferfishRainManager,
      LlamaHunterManager, TemperatureManager, WitchSummonManager, ChickenDamageListener,
      GrassPoisonListener, SnowListener, EndStoneListener, GhastSpawnerListener,
      RavagerSpawnerListener, SkeletonWitherListener, BlazeAttackListener,
      ZombifyTradeListener, ForestFireListener, WitherRoseListener, DoorDamageListener,
      ButtonDamageListener, LevitationListener, PressurePlateFireListener,
      SlowSlabListener, LavaLaunchListener, MimicListener, EggArrowListener,
      EggDropListener, WitherArmorListener, GlassLuckListener, EndDirtListener,
      EndCrystalListener, PillagerGunListener, FallDamageListener, ChargedCreeperListener,
      DrownedSpawnerListener, GravestoneListener, AxolotlListener, GoddessHoeListener,
      OldAgeListener, NamedZombieListener, WanderingTraderListener, ZombieSpeedListener,
      ShieldSurfListener, WitherBowListener, CatBatListener, BambooSpreadListener,
      ZombieDrowningListener, EscalationListener, ButterfingersListener,
      SpillageListener, EggshellsListener, DeathScoreboardListener,
      GhastBurnRunnable, SandAttackRunnable, FreeCookieRunnable, SilverfishBurnRunnable,
      DirtRecipeFeature, StoneRecipeDeleter, WeepingAngelManagerFactory, AnvilRunnableFactory,
      DripstoneManagerFactory, ExplosiveArrowManager, BedtimeManager, CakeListenerFactory,
      EggListenerFactory, OvergrowthListenerFactory(OvergrowthListener::randomWood),
      ClassicLavaManager, GhastLavaListenerFactory(ClassicLavaManager.STORAGE_IGNORER_KEY)
    )

  fun createComposite(builderState: BuilderState): FeatureContainer =
    FeatureContainerFactory.createComposite(this.allFactories, builderState)

}
