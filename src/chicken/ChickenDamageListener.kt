
package com.mercerenies.turtletroll.chicken

import com.mercerenies.turtletroll.Weight
import com.mercerenies.turtletroll.sample
import com.mercerenies.turtletroll.feature.AbstractFeature

import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.entity.EntitySpawnEvent
import org.bukkit.event.world.ChunkPopulateEvent
import org.bukkit.entity.Chicken
import org.bukkit.entity.EntityType

import kotlin.random.Random

class ChickenDamageListener(
  val bannedMobs: Set<EntityType> = DEFAULT_BANNED_MOBS
) : AbstractFeature(), Listener {
  // We don't want onEntitySpawn to trigger on our *own* EntitySpawn
  private var recursionBlock = false

  companion object {
    val DEFAULT_BANNED_MOBS = setOf(
      EntityType.COW, EntityType.PIG, EntityType.LLAMA,
      EntityType.BAT, EntityType.DONKEY, EntityType.HORSE,
      EntityType.MULE, EntityType.PARROT, EntityType.SHEEP,
      EntityType.CHICKEN, EntityType.RABBIT,
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
  fun onEntitySpawn(event: EntitySpawnEvent) {
    if (!isEnabled()) {
      return
    }
    val entity = event.entity
    if ((!recursionBlock) && (bannedMobs.contains(entity.type))) {

      // This special check for bats specifically is to prevent the
      // server from becoming overloaded
      if ((entity.type == EntityType.BAT) && (Random.nextDouble() < 0.1)) {
        return
      }

      event.setCancelled(true)
      recursionBlock = true
      try {
        event.location.world!!.spawnEntity(event.location, EntityType.CHICKEN)
      } finally {
        recursionBlock = false
      }
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

}
