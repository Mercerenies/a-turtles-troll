
package com.mercerenies.turtletroll.transformed

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
import org.bukkit.util.Vector

import kotlin.random.Random

class GhastSpawnerListener(
  override val chance: Double = 0.2
) : TransformedSpawnerListener {

  override val targetEntity: EntityType = EntityType.GHAST

  override val offset: Vector = Vector(0.0, 2.0, 0.0)

  override fun shouldAttemptReplace(event: EntitySpawnEvent): Boolean {
    val entity = event.entity
    val world = event.location.world!!
    if ((HOSTILES.contains(entity.type)) && (WORLDS.contains(world.environment))) {
      if ((event.location.isExposedToSky()) || (world.environment == World.Environment.THE_END)) {
        return true
      }
    }
    return false
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

  companion object {

    val HOSTILES = setOf(
      EntityType.ZOMBIE, EntityType.CREEPER, EntityType.SKELETON, EntityType.SPIDER,
      EntityType.ENDERMAN, EntityType.WITCH,
    )

    val WORLDS = setOf(
      World.Environment.NORMAL, World.Environment.THE_END,
    )

  }

}
