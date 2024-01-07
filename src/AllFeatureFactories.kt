
package com.mercerenies.turtletroll

import com.mercerenies.turtletroll.feature.container.FeatureContainer
import com.mercerenies.turtletroll.feature.container.CompositeFeatureContainer
import com.mercerenies.turtletroll.feature.container.AbstractFeatureContainer
import com.mercerenies.turtletroll.feature.builder.FeatureContainerFactory
import com.mercerenies.turtletroll.feature.builder.BuilderState
import com.mercerenies.turtletroll.banish.BanishmentManagerFactory
import com.mercerenies.turtletroll.parrot.ParrotCookieListener
import com.mercerenies.turtletroll.parrot.ParrotDeathListener
import com.mercerenies.turtletroll.parrot.ParrotManager
import com.mercerenies.turtletroll.chicken.ChickenDamageListener
import com.mercerenies.turtletroll.birch.BirchGlareRunnableFactory
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
import com.mercerenies.turtletroll.drop.BlockBreakEventListener
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
import com.mercerenies.turtletroll.happening.RandomEventRunnable
import com.mercerenies.turtletroll.happening.NothingEvent
import com.mercerenies.turtletroll.happening.event.PufferfishRainManager
import com.mercerenies.turtletroll.happening.event.SpatialRendFeature

object AllFeatureFactories {

  val NULL_RANDOM_EVENT_WEIGHT = 70.0

  private val allFactories: List<FeatureContainerFactory<FeatureContainer>> =
    listOf(
      // Independent features
      AllayManager,
      AmethystBlockDropFactory,
      AngryGolemManager,
      AnvilRunnableFactory,
      ApacheBeeListener,
      AxolotlListener,
      BambooSpreadListener,
      BanishmentManagerFactory,
      BedrockFeatureFactory,
      BeeAttackFeatureFactory,
      BlazeAttackListener,
      BlazeEyeManager,
      BloodListener,
      BoatBombListener,
      BouncyProjectileListener,
      BucketRouletteRunnable,
      ButterfingersListener,
      ButtonDamageListener,
      CakeListenerFactory,
      CancelDropFeatureFactory,
      CarefulHandsListener,
      CatBatListener,
      ChainmailRecipeFeature,
      ChargedCreeperListener,
      ChestShuffleListener,
      ChickenDamageListener,
      ContagiousMossManager,
      CreeperDeathListener,
      DeathScoreboardListener,
      DirtDropFeatureFactory,
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
      EndStoneListener,
      EnderChestListener,
      EndermanGodListener,
      EndermiteSpawnFeatureFactory,
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
      MelompkinFeatureFactory,
      MinecraftTriviaManagerFactory,
      MossRevengeFeatureFactory,
      NamedZombieListener,
      NetherrackBoomFeatureFactory,
      NiceListener,
      ObsidianWallListener,
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
      ShuffleLogsFeatureFactory,
      SilverfishAttackFeatureFactory,
      SilverfishBurnRunnable,
      SinkholeListener,
      SkeletonWitherListener,
      SlimeSplitListener,
      SlowSlabListener,
      SnowListener,
      SnowballListener,
      SolidSwapListener,
      SpatialRendFeature,
      SpillageListener,
      StoneRecipeDeleter,
      StrongholdAttackFeatureFactory,
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
      BirchGlareRunnableFactory(CustomDeathMessageListener.DEATH_MESSAGE_KEY),
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
    val allFeatures = FeatureContainerFactory.createComposite(this.allFactories, builderState) {
      CompositeFeatureContainer(it)
    }

    // BlockBreakEventListener
    val blockBreakEventListener = BlockBreakEventListener(allFeatures)
    val blockBreakEventListenerContainer = object : AbstractFeatureContainer() {
      override val listeners = listOf(blockBreakEventListener)
    }

    // RandomEventRunnable
    val allRandomEvents = allFeatures.randomEvents + NothingEvent(NULL_RANDOM_EVENT_WEIGHT)
    val randomEventRunnable = RandomEventRunnable(builderState.plugin, allRandomEvents)
    val randomEventRunnableContainer = object : AbstractFeatureContainer() {
      override val gameModifications = listOf(randomEventRunnable)
      override val debugCommands = listOf(
        "event" to randomEventRunnable.debugFireCommand,
        "eventstatus" to randomEventRunnable.debugStatusCommand,
      )
    }

    return CompositeFeatureContainer(
      listOf(blockBreakEventListenerContainer, randomEventRunnableContainer, allFeatures),
    )
  }

}
