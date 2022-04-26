
package com.mercerenies.turtletroll

import com.mercerenies.turtletroll.feature.container.DropFeatureContainer
import com.mercerenies.turtletroll.feature.container.FeatureContainer
import com.mercerenies.turtletroll.feature.container.CompositeFeatureContainer
import com.mercerenies.turtletroll.feature.container.CompositeDropFeatureContainer
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
import com.mercerenies.turtletroll.drop.DirtDropFeatureFactory
import com.mercerenies.turtletroll.drop.SilverfishAttackFeatureFactory
import com.mercerenies.turtletroll.drop.BeeAttackFeatureFactory
import com.mercerenies.turtletroll.drop.AmethystBlockDropFactory
import com.mercerenies.turtletroll.drop.BedrockFeatureFactory
import com.mercerenies.turtletroll.drop.CancelDropFeatureFactory
import com.mercerenies.turtletroll.drop.EndermiteSpawnFeatureFactory
import com.mercerenies.turtletroll.drop.NetherrackBoomFeatureFactory
import com.mercerenies.turtletroll.drop.ShuffleLogsFeatureFactory
import com.mercerenies.turtletroll.drop.StrongholdAttackFeatureFactory

object AllFeatureFactories {

  private val allDropFactories: List<FeatureContainerFactory<DropFeatureContainer>> =
    listOf(
      // Drop features
      AmethystBlockDropFactory,
      BedrockFeatureFactory,
      BeeAttackFeatureFactory,
      BeeAttackFeatureFactory,
      CancelDropFeatureFactory,
      DirtDropFeatureFactory,
      EndermiteSpawnFeatureFactory,
      MelompkinFeatureFactory,
      NetherrackBoomFeatureFactory,
      ShuffleLogsFeatureFactory,
      SilverfishAttackFeatureFactory,
      StrongholdAttackFeatureFactory,
    )

  private val allFactories: List<FeatureContainerFactory<FeatureContainer>> =
    listOf(
      // Independent features
      AnvilRunnableFactory,
      AxolotlListener,
      BambooSpreadListener,
      BedtimeManager,
      BlazeAttackListener,
      ButterfingersListener,
      ButtonDamageListener,
      CakeListenerFactory,
      CatBatListener,
      ChargedCreeperListener,
      ChickenDamageListener,
      ContagiousMossManager,
      DeathScoreboardListener,
      DirtRecipeFeature,
      DoorDamageListener,
      DragonBombManager,
      DripstoneManagerFactory,
      DrownedSpawnerListener,
      EggArrowListener,
      EggDropListener,
      EggListenerFactory,
      EggshellsListener,
      EndCrystalListener,
      EndDirtListener,
      EndStoneListener,
      EscalationListener,
      ExplosiveArrowManager,
      FallDamageListener,
      ForestFireListener,
      FreeCookieRunnable,
      GhastBurnRunnable,
      GhastSpawnerListener,
      GlassLuckListener,
      GoddessHoeListener,
      GrassPoisonListener,
      GravestoneListener,
      LavaLaunchListener,
      LevitationListener,
      LlamaHunterManager,
      MimicListener,
      NamedZombieListener,
      OldAgeListener,
      OvergrowthListenerFactory(OvergrowthListener::randomWood),
      PetPhantomManager,
      PillagerGunListener,
      PressurePlateFireListener,
      PufferfishRainManager,
      RavagerSpawnerListener,
      SandAttackRunnable,
      ShieldSurfListener,
      SilverfishBurnRunnable,
      SkeletonWitherListener,
      SlowSlabListener,
      SnowListener,
      SpillageListener,
      StoneRecipeDeleter,
      TemperatureManager,
      WanderingTraderListener,
      WeepingAngelManagerFactory,
      WitchSummonManager,
      WitherArmorListener,
      WitherBowListener,
      WitherRoseListener,
      ZombieDrowningListener,
      ZombieSpeedListener,
      ZombifyTradeListener,
      // Classic lava and the things that depend on it
      ClassicLavaManager,
      GhastLavaListenerFactory(ClassicLavaManager.STORAGE_IGNORER_KEY),
      // Pumpkin slowness and the things that depend on it
      PumpkinSlownessManager,
      BreakLightOnSightListenerFactory(PumpkinSlownessManager.PUMPKIN_FEATURE_KEY),
      ElectricWaterListenerFactory(PumpkinSlownessManager.PUMPKIN_FEATURE_KEY),
    )

  fun createComposite(builderState: BuilderState): FeatureContainer {
    val dropFeatures = FeatureContainerFactory.createComposite(this.allDropFactories, builderState, { CompositeDropFeatureContainer(it) })
    val regularFeatures = FeatureContainerFactory.createComposite(this.allFactories, builderState, { CompositeFeatureContainer(it) })
    return CompositeFeatureContainer(
      listOf(blockBreakListenerContainer(dropFeatures), regularFeatures),
    )
  }

}
