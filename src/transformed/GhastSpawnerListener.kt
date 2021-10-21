
package com.mercerenies.turtletroll.transformed

import com.mercerenies.turtletroll.ext.*
import com.mercerenies.turtletroll.SpawnReason
import com.mercerenies.turtletroll.ReplaceMobsRunnable

import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.CreatureSpawnEvent
import org.bukkit.event.world.ChunkPopulateEvent
import org.bukkit.entity.Ghast
import org.bukkit.entity.EntityType
import org.bukkit.entity.Entity
import org.bukkit.World
import org.bukkit.Chunk
import org.bukkit.util.Vector
import org.bukkit.plugin.Plugin

import kotlin.random.Random

class GhastSpawnerListener(
  val plugin: Plugin,
  override val chance: Double = 0.2,
) : TransformedSpawnerListener() {

  override val name = "ghasts"

  override val description = "Allows ghasts to spawn in all dimensions"

  override val targetEntity: EntityType = EntityType.GHAST

  override val offset: Vector = Vector(0.0, 2.0, 0.0)

  private inner class EndermenToGhasts(_chunk: Chunk) : ReplaceMobsRunnable(_chunk) {
    override fun replaceWith(entity: Entity): EntityType? =
      if (entity.type == EntityType.ENDERMAN) {
        EntityType.GHAST
      } else {
        null
      }
  }

  override fun shouldAttemptReplace(event: CreatureSpawnEvent): Boolean {
    if (!SpawnReason.isNatural(event)) {
      return false
    }

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
    if (!isEnabled()) {
      return
    }
    val chunk = event.getChunk()
    EndermenToGhasts(chunk).schedule(plugin)
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
