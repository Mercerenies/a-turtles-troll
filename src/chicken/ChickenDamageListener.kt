
package com.mercerenies.turtletroll.chicken

import com.mercerenies.turtletroll.Weight
import com.mercerenies.turtletroll.sample
import com.mercerenies.turtletroll.feature.AbstractFeature
import com.mercerenies.turtletroll.SpawnReason

import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.entity.CreatureSpawnEvent
import org.bukkit.event.world.ChunkPopulateEvent
import org.bukkit.entity.Chicken
import org.bukkit.entity.Zombie
import org.bukkit.entity.EntityType
import org.bukkit.Location

import kotlin.random.Random

class ChickenDamageListener(
  val bannedMobs: Set<EntityType> = DEFAULT_BANNED_MOBS,
  val zombieRiderChance: Double = 0.1,
) : AbstractFeature(), Listener {
  companion object {

    val DEFAULT_BANNED_MOBS = setOf(
      EntityType.COW, EntityType.PIG, EntityType.LLAMA,
      EntityType.DONKEY, EntityType.HORSE,
      EntityType.MULE, EntityType.PARROT, EntityType.SHEEP,
      EntityType.RABBIT,
    )

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
      victim.health = 0.0
      victim.world.createExplosion(victim.location, 5.0F, true)
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
    val entities = event.chunk.entities
    for (entity in entities) {
      if (bannedMobs.contains(entity.type)) {
        entity.location.world!!.spawnEntity(entity.location, EntityType.CHICKEN)
        entity.remove()
      }
    }
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
