
package com.mercerenies.turtletroll

import com.mercerenies.turtletroll.ext.*
import com.mercerenies.turtletroll.Weight
import com.mercerenies.turtletroll.sample

import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntitySpawnEvent
import org.bukkit.event.world.ChunkPopulateEvent
import org.bukkit.entity.Ghast
import org.bukkit.entity.EntityType
import org.bukkit.World

import kotlin.random.Random

class GhastSpawnerListener(val chance: Double = 0.2) : Listener {

  companion object {

    val HOSTILES = setOf(
      EntityType.ZOMBIE, EntityType.CREEPER, EntityType.SKELETON, EntityType.SPIDER,
      EntityType.ENDERMAN, EntityType.WITCH,
    )

    val WORLDS = setOf(
      World.Environment.NORMAL, World.Environment.THE_END,
    )

  }

  @EventHandler
  fun onEntitySpawn(event: EntitySpawnEvent) {
    val entity = event.entity
    val world = event.location.world!!
    // To replace an entity, the following conditions must be met:
    // 1. The entity must be a common hostile mob
    // 2. The world must not be the nether
    // 3. The target location must be exposed to the sky (or anywhere, in the end)
    // 4. A random chance must be rolled
    if ((HOSTILES.contains(entity.type)) && (WORLDS.contains(world.environment))) {
      if ((event.location.isExposedToSky()) || (world.environment == World.Environment.THE_END)) {
        if (Random.nextDouble() < chance) {
          event.setCancelled(true)
          event.location.world!!.spawnEntity(event.location.add(0.0, 2.0, 0.0), EntityType.GHAST)
        }
      }
    }
  }

  @EventHandler
  fun onChunkPopulate(event: ChunkPopulateEvent) {
    val entities = event.chunk.entities
    for (entity in entities) {
      if (entity.type == EntityType.ENDERMAN) {
        entity.location.world!!.spawnEntity(entity.location, EntityType.GHAST)
        entity.remove()
      }
    }
  }

}
