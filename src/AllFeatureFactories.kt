
package com.mercerenies.turtletroll

import com.mercerenies.turtletroll.feature.container.DropFeatureContainer
import com.mercerenies.turtletroll.feature.container.FeatureContainer
import com.mercerenies.turtletroll.feature.container.CompositeFeatureContainer
import com.mercerenies.turtletroll.feature.container.CompositeDropFeatureContainer
import com.mercerenies.turtletroll.feature.builder.FeatureContainerFactory
import com.mercerenies.turtletroll.feature.builder.BuilderState
import com.mercerenies.turtletroll.banish.BanishmentManagerFactory
import com.mercerenies.turtletroll.parrot.ParrotCookieListener
import com.mercerenies.turtletroll.parrot.ParrotDeathListener
import com.mercerenies.turtletroll.parrot.ParrotManager
import com.mercerenies.turtletroll.chicken.ChickenDamageListener
import com.mercerenies.turtletroll.rain.RainwaterManagerFactory
import com.mercerenies.turtletroll.transformed.GhastSpawnerListener
import com.mercerenies.turtletroll.transformed.RavagerSpawnerListener
import com.mercerenies.turtletroll.transformed.DrownedSpawnerListener
import com.mercerenies.turtletroll.durability.DoorDamageListener
import com.mercerenies.turtletroll.durability.ButtonDamageListener
import com.mercerenies.turtletroll.mimic.MimicListenerFactory
import com.mercerenies.turtletroll.gravestone.CustomDeathMessageListener
import com.mercerenies.turtletroll.gravestone.GravestoneListener
import com.mercerenies.turtletroll.gravestone.DeathScoreboardListener
import com.mercerenies.turtletroll.demand.DailyDemandManagerFactory
import com.mercerenies.turtletroll.spillage.SpillageListener
import com.mercerenies.turtletroll.falling.SandAttackRunnable
import com.mercerenies.turtletroll.cookie.FreeCookieRunnable
import com.mercerenies.turtletroll.cookie.CookieListenerFactory
import com.mercerenies.turtletroll.recipe.DirtRecipeFeature
import com.mercerenies.turtletroll.recipe.StoneRecipeDeleter
import com.mercerenies.turtletroll.recipe.ChainmailRecipeFeature
import com.mercerenies.turtletroll.angel.WeepingAngelManagerFactory
import com.mercerenies.turtletroll.falling.AnvilRunnableFactory
import com.mercerenies.turtletroll.dripstone.DripstoneManagerFactory
import com.mercerenies.turtletroll.cake.CakeListenerFactory
import com.mercerenies.turtletroll.egg.EggListenerFactory
import com.mercerenies.turtletroll.overgrowth.OvergrowthListenerFactory
import com.mercerenies.turtletroll.ghastlava.GhastLavaListenerFactory
import com.mercerenies.turtletroll.drop.MossRevengeFeatureFactory
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
import com.mercerenies.turtletroll.pokeball.PokeballManager
import com.mercerenies.turtletroll.jump.EncumbranceManagerFactory
import com.mercerenies.turtletroll.trivia.MinecraftTriviaManagerFactory
import com.mercerenies.turtletroll.blazeeye.BlazeEyeManager

object AllFeatureFactories {

  private val allDropFactories: List<FeatureContainerFactory<DropFeatureContainer>> =
    listOf(
      // Drop features
      AmethystBlockDropFactory,
      BedrockFeatureFactory,
      BeeAttackFeatureFactory,
      CancelDropFeatureFactory,
      DirtDropFeatureFactory,
      EndermiteSpawnFeatureFactory,
      MelompkinFeatureFactory,
      MossRevengeFeatureFactory,
      NetherrackBoomFeatureFactory,
      ShuffleLogsFeatureFactory,
      SilverfishAttackFeatureFactory,
      StrongholdAttackFeatureFactory,
    )

  private val allFactories: List<FeatureContainerFactory<FeatureContainer>> =
    listOf(
      // Independent features
      AllayManager,
      AngryGolemManager,
      AnvilRunnableFactory,
      ApacheBeeListener,
      AxolotlListener,
      BambooSpreadListener,
      BanishmentManagerFactory,
      BlazeAttackListener,
      BlazeEyeManager,
      BloodListener,
      BoatBombListener,
      BouncyProjectileListener,
      BucketRouletteRunnable,
      ButterfingersListener,
      ButtonDamageListener,
      CakeListenerFactory,
      CarefulHandsListener,
      CatBatListener,
      ChainmailRecipeFeature,
      ChargedCreeperListener,
      ChestShuffleListener,
      ChickenDamageListener,
      ContagiousMossManager,
      CreeperDeathListener,
      DeathScoreboardListener,
      DirtRecipeFeature,
      DoctorDancesManager,
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
      EnderChestListener,
      EndermanGodListener,
      EndStoneListener,
      EscalationListener,
      ExpirationDateListener,
      ExplosiveArrowManager,
      FallDamageListener,
      FeastListener,
      FishHookListener,
      FishSanctuaryListener,
      ForestFireListener,
      FreeCookieRunnable,
      FunHatListener,
      GhastBurnRunnable,
      GhastSpawnerListener,
      GlassLuckListener,
      GoddessHoeListener,
      GrassPoisonListener,
      GrassSpreadListener,
      GravestoneListener,
      GrievingWidowListener,
      JohnnyListener,
      KillerRabbitListener,
      LavaLaunchListener,
      LearningFromFailureListener,
      LevitationListener,
      LlamaHunterManager,
      MinecraftTriviaManagerFactory,
      NamedZombieListener,
      ObsidianWallListener, /////
      OvergrowthListenerFactory.Default(),
      ParrotCookieListener,
      ParrotDeathListener,
      ParrotManager,
      PetPhantomManager,
      PiglinBarterListener,
      PillagerDeathListener,
      PillagerGunListener,
      PokeballManager,
      PressurePlateFireListener,
      PufferfishRainManager,
      RavagerSpawnerListener,
      SaddleListener,
      SandAttackRunnable,
      SheepColorListener,
      ShieldSurfListener,
      SilverfishBurnRunnable,
      SinkholeListener,
      SkeletonWitherListener,
      SlimeSplitListener,
      SlowSlabListener,
      SnowListener,
      SnowballListener,
      SolidSwapListener,
      SpillageListener,
      StoneRecipeDeleter,
      SweetDreamsListener,
      UnfinishedBusinessListener,
      VillagerDeathListener,
      WanderingTraderListener,
      WardenSummonRunnable,
      WitchAttackListener,
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
      CactusKickListenerFactory(PumpkinSlownessManager.PUMPKIN_FEATURE_KEY),
      ElectricWaterListenerFactory(PumpkinSlownessManager.PUMPKIN_FEATURE_KEY),
      ExplodingNyliumListenerFactory(PumpkinSlownessManager.PUMPKIN_FEATURE_KEY),
      // Custom death message and the things that depend on it
      CustomDeathMessageListener,
      CookieListenerFactory.Default(CustomDeathMessageListener.DEATH_MESSAGE_KEY),
      MimicListenerFactory(CustomDeathMessageListener.DEATH_MESSAGE_KEY),
      OldAgeListenerFactory(CustomDeathMessageListener.DEATH_MESSAGE_KEY),
      RainwaterManagerFactory(CustomDeathMessageListener.DEATH_MESSAGE_KEY),
      RedstoneWorldListenerFactory(CustomDeathMessageListener.DEATH_MESSAGE_KEY),
      TemperatureManagerFactory(CustomDeathMessageListener.DEATH_MESSAGE_KEY),
      WeepingAngelManagerFactory(CustomDeathMessageListener.DEATH_MESSAGE_KEY),
      // Daily demands (gods) manager and the things that depend on it
      DailyDemandManagerFactory(DailyDemandManagerFactory::bowserEventSelector),
      EncumbranceManagerFactory.Default(DailyDemandManagerFactory.GODS_FEATURE_KEY),
    )

  fun createComposite(builderState: BuilderState): FeatureContainer {
    val dropFeatures = FeatureContainerFactory.createComposite(this.allDropFactories, builderState, { CompositeDropFeatureContainer(it) })
    val regularFeatures = FeatureContainerFactory.createComposite(this.allFactories, builderState, { CompositeFeatureContainer(it) })
    return CompositeFeatureContainer(
      listOf(blockBreakListenerContainer(dropFeatures), regularFeatures),
    )
  }

}
