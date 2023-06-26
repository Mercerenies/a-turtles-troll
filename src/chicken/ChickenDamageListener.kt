
package com.mercerenies.turtletroll.chicken

import com.mercerenies.turtletroll.feature.AbstractFeature
import com.mercerenies.turtletroll.feature.container.FeatureContainer
import com.mercerenies.turtletroll.feature.container.ListenerContainer
import com.mercerenies.turtletroll.feature.builder.BuilderState
import com.mercerenies.turtletroll.feature.builder.FeatureContainerFactory
import com.mercerenies.turtletroll.SpawnReason
import com.mercerenies.turtletroll.ReplaceMobsRunnable

import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.entity.CreatureSpawnEvent
import org.bukkit.event.world.ChunkPopulateEvent
import org.bukkit.entity.Chicken
import org.bukkit.entity.PufferFish
import org.bukkit.entity.Zombie
import org.bukkit.entity.EntityType
import org.bukkit.entity.Entity
import org.bukkit.Location
import org.bukkit.Chunk
import org.bukkit.GameRule
import org.bukkit.plugin.Plugin

import kotlin.random.Random

class ChickenDamageListener(
  val plugin: Plugin,
  val bannedMobs: Set<EntityType> = DEFAULT_BANNED_MOBS,
  val zombieRiderChance: Double = 0.1,
) : AbstractFeature(), Listener {

  companion object : FeatureContainerFactory<FeatureContainer> {

    val DEFAULT_BANNED_MOBS = setOf(
      EntityType.COW,
      EntityType.DONKEY, EntityType.HORSE,
      EntityType.MULE, EntityType.PARROT, EntityType.SHEEP,
    )

    override fun create(state: BuilderState): FeatureContainer =
      ListenerContainer(
        ChickenDamageListener(
          plugin = state.plugin,
          zombieRiderChance = state.config.getDouble("chickens.zombie_rider_chance"),
        )
      )

  }

  private inner class ReplaceWithChicken(_chunk: Chunk) : ReplaceMobsRunnable(_chunk) {
    override fun replaceWith(entity: Entity): EntityType? =
      if (bannedMobs.contains(entity.type)) {
        EntityType.CHICKEN
      } else {
        null
      }
  }

  override val name = "chickens"

  override val description = "Replaces passive mobs with chickens and makes chickens explode"

  @EventHandler
  fun onEntityDamageByEntity(event: EntityDamageByEntityEvent) {
    if (!isEnabled()) {
      return
    }
    val victim = event.entity
    if (victim is Chicken) {
      if (event.damager is PufferFish) {
        event.setCancelled(true)
      } else {
        val world = victim.world
        victim.health = 0.0
        if (world.getGameRuleValue(GameRule.MOB_GRIEFING) ?: true) {
          world.createExplosion(victim.location, 5.0F, true, true)
        } else {
          world.createExplosion(victim.location, 5.0F, true, false)
        }
      }
    }
  }

  @EventHandler
  fun onCreatureSpawn(event: CreatureSpawnEvent) {
    if (!isEnabled()) {
      return
    }
    if (!SpawnReason.isNatural(event)) {
      return
    }
    val entity = event.entity
    if (bannedMobs.contains(entity.type)) {
      event.setCancelled(true)
      val world = event.location.world!!
      val chicken = world.spawnEntity(event.location, EntityType.CHICKEN) as Chicken
      considerAddingRider(event.location, chicken)
    } else if (entity is Chicken) {
      considerAddingRider(event.location, entity)
    }
  }

  @EventHandler
  fun onChunkPopulate(event: ChunkPopulateEvent) {
    if (!isEnabled()) {
      return
    }
    val chunk = event.getChunk()
    ReplaceWithChicken(chunk).schedule(plugin)
  }

  private fun considerAddingRider(location: Location, chicken: Chicken) {
    // Consider spawning a rider
    if (Random.nextDouble() < zombieRiderChance) {
      val rider = location.world!!.spawnEntity(location, EntityType.ZOMBIE) as Zombie
      rider.setBaby()
      chicken.addPassenger(rider)
    }
  }

}
