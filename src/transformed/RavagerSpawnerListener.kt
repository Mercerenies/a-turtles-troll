
package com.mercerenies.turtletroll.transformed

import com.mercerenies.turtletroll.ext.*
import com.mercerenies.turtletroll.Weight
import com.mercerenies.turtletroll.sample

import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntitySpawnEvent
import org.bukkit.event.world.ChunkPopulateEvent
import org.bukkit.entity.EntityType
import org.bukkit.World
import org.bukkit.util.Vector

import kotlin.random.Random

class RavagerSpawnerListener(
  override val chance: Double = 0.5
) : TransformedSpawnerListener() {

  override val name = "ravagers"

  override val description = "Allows ravagers to spawn in the Nether"

  override val targetEntity: EntityType = EntityType.RAVAGER

  override fun shouldAttemptReplace(event: EntitySpawnEvent): Boolean =
    event.entity.type == EntityType.HOGLIN

  @EventHandler
  fun onChunkPopulate(event: ChunkPopulateEvent) {
    if (!isEnabled()) {
      return
    }
    val entities = event.chunk.entities
    for (entity in entities) {
      if (entity.type == EntityType.HOGLIN) {
        if (Random.nextDouble() < chance) {
          entity.location.world!!.spawnEntity(entity.location, EntityType.RAVAGER)
          entity.remove()
        }
      }
    }
  }

}
