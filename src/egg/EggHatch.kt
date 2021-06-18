
package com.mercerenies.turtletroll.egg

import com.mercerenies.turtletroll.Weight
import com.mercerenies.turtletroll.sample
import com.mercerenies.turtletroll.feature.AbstractFeature

import org.bukkit.util.Vector
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.ProjectileHitEvent
import org.bukkit.event.entity.CreatureSpawnEvent
import org.bukkit.event.player.PlayerEggThrowEvent
import org.bukkit.entity.EntityType
import org.bukkit.entity.*
import org.bukkit.material.Colorable
import org.bukkit.DyeColor
import org.bukkit.plugin.Plugin

import kotlin.random.Random

// Helper for common egg hatch effects
object EggHatch {

  // Intentionally omitted: Elder guardian, Ender dragon, Wither
  fun defaultEffects(plugin: Plugin) = listOf(
    Weight(BatFlockEffect(6), 2.0),
    Weight(SpawnEntityEffect(Cat::class).maybeBaby(0.1).andThen(this::randomizeCatData), 1.2),
    Weight(SpawnEntityEffect(Chicken::class).maybeBaby(0.1), 1.0),
    Weight(SpawnEntityEffect(Cod::class), 2.5),
    Weight(SpawnEntityEffect(Cow::class).maybeBaby(0.1), 0.4),
    Weight(SpawnEntityEffect(Donkey::class).maybeBaby(0.1), 0.4),
    Weight(SpawnEntityEffect(Fox::class).maybeBaby(0.1), 0.4),
    Weight(SpawnEntityEffect(Horse::class).maybeBaby(0.1), 0.4),
    Weight(SpawnEntityEffect(MushroomCow::class).maybeBaby(0.1).andThen(this::randomizeMushroomCow), 0.6),
    Weight(SpawnEntityEffect(Mule::class).maybeBaby(0.1), 0.4),
    Weight(SpawnEntityEffect(Ocelot::class), 0.4),
    Weight(SpawnEntityEffect(Parrot::class), 4.0),
    Weight(SpawnEntityEffect(Pig::class).maybeBaby(0.1), 0.4),
    Weight(SpawnEntityEffect(Piglin::class).maybeBaby(0.1), 0.8),
    Weight(SpawnEntityEffect(PolarBear::class).maybeBaby(0.1), 0.5),
    Weight(SpawnEntityEffect(PufferFish::class), 4.0),
    Weight(SpawnEntityEffect(Rabbit::class).maybeBaby(0.1), 0.4),
    Weight(SpawnEntityEffect(Salmon::class), 4.0),
    Weight(SpawnEntityEffect(Sheep::class).maybeBaby(0.1).andThen(this::randomizeColor), 0.4),
    Weight(SkeletonTrapEffect(3, plugin), 0.2),
    Weight(SpawnEntityEffect(Snowman::class), 0.9),
    Weight(SpawnEntityEffect(Squid::class), 4.0),
    Weight(SpawnEntityEffect(Strider::class).maybeBaby(0.1), 0.7),
    Weight(SpawnEntityEffect(TropicalFish::class), 4.0),
    Weight(SpawnEntityEffect(Turtle::class).maybeBaby(0.1), 0.2),
    Weight(SpawnEntityEffect(Villager::class).maybeBaby(0.1), 0.2),
    Weight(SpawnEntityEffect(WanderingTrader::class).maybeBaby(0.1), 0.2),
    Weight(SpawnEntityEffect(Bee::class).andThen { it.setAnger(100) }, 1.3),
    Weight(SpawnEntityEffect(CaveSpider::class), 0.7),
    Weight(SpawnEntityEffect(Dolphin::class), 1.6),
    Weight(SpawnEntityEffect(Enderman::class), 0.7),
    Weight(SpawnEntityEffect(IronGolem::class), 0.5),
    Weight(SpawnEntityEffect(Llama::class).maybeBaby(0.1), 1.6),
    Weight(SpawnEntityEffect(Panda::class).maybeBaby(0.1), 0.5),
    Weight(SpawnEntityEffect(Spider::class), 0.7),
    Weight(SpawnEntityEffect(Wolf::class).maybeBaby(0.1), 1.8),
    Weight(SpawnEntityEffect(PigZombie::class), 0.6),
    Weight(SpawnEntityEffect(Blaze::class), 0.7),
    Weight(SpawnEntityEffect(Creeper::class), 0.5),
    Weight(SpawnEntityEffect(Drowned::class), 0.7),
    Weight(SpawnEntityEffect(Endermite::class), 0.7),
    Weight(SpawnEntityEffect(Evoker::class), 0.3),
    Weight(SpawnEntityEffect(Ghast::class), 0.4),
    Weight(SpawnEntityEffect(Guardian::class), 0.5),
    Weight(SpawnEntityEffect(Hoglin::class), 0.4),
    Weight(SpawnEntityEffect(Husk::class), 0.7),
    Weight(SpawnEntityEffect(MagmaCube::class).andThen(this::randomizeSize), 0.7),
    Weight(SpawnEntityEffect(Phantom::class).andThen(this::randomizeSize), 0.6),
    Weight(SpawnEntityEffect(PiglinBrute::class), 0.2),
    Weight(SpawnEntityEffect(Pillager::class), 0.6),
    Weight(SpawnEntityEffect(Ravager::class), 0.3),
    Weight(SpawnEntityEffect(Shulker::class), 0.2),
    Weight(SpawnEntityEffect(Silverfish::class), 0.7),
    Weight(SpawnEntityEffect(Skeleton::class), 0.7),
    Weight(SpawnEntityEffect(Slime::class).andThen(this::randomizeSize), 0.7),
    Weight(SpawnEntityEffect(Stray::class), 0.8),
    Weight(SpawnEntityEffect(Vex::class), 0.9),
    Weight(SpawnEntityEffect(Vindicator::class), 0.7),
    Weight(SpawnEntityEffect(Witch::class), 0.8),
    Weight(SpawnEntityEffect(WitherSkeleton::class), 0.2),
    Weight(SpawnEntityEffect(Zoglin::class), 0.6),
    Weight(SpawnEntityEffect(Zombie::class).maybeBaby(0.1), 1.1),
    Weight(SpawnEntityEffect(ZombieVillager::class), 0.4),
    Weight(SpawnEntityEffect(Giant::class), 0.2),
    Weight(SpawnEntityEffect(ZombieHorse::class), 1.3),
    Weight(SpawnEntityEffect(Illusioner::class), 0.2),
  )

  private fun randomizeCatData(entity: Cat) {
    entity.catType = Cat.Type.values().random()
    entity.collarColor = DyeColor.values().random()
  }

  private fun randomizeColor(entity: Colorable) {
    entity.setColor(DyeColor.values().random())
  }

  private fun randomizeMushroomCow(entity: MushroomCow) {
    entity.setVariant(MushroomCow.Variant.values().random())
  }

  private fun randomizeSize(entity: Slime) {
    entity.setSize(listOf(1, 2, 3, 4).random())
  }

  private fun randomizeSize(entity: Phantom) {
    entity.setSize(listOf(1, 2, 3, 4).random())
  }

}
