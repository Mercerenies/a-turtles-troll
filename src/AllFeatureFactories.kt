
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
import com.mercerenies.turtletroll.spillage.SpillageListener

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
    )

  fun createComposite(builderState: BuilderState): FeatureContainer =
    FeatureContainerFactory.createComposite(this.allFactories, builderState)

}