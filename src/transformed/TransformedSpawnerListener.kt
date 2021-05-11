
package com.mercerenies.turtletroll.transformed

import com.mercerenies.turtletroll.ext.*
import com.mercerenies.turtletroll.Weight
import com.mercerenies.turtletroll.sample

import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntitySpawnEvent
import org.bukkit.event.world.ChunkPopulateEvent
import org.bukkit.entity.Entity
import org.bukkit.entity.EntityType
import org.bukkit.World
import org.bukkit.util.Vector

import kotlin.random.Random

interface TransformedSpawnerListener : Listener {

  val chance: Double

  val targetEntity: EntityType

  val offset: Vector
    get() = Vector(0.0, 0.0, 0.0)

  fun shouldAttemptReplace(event: EntitySpawnEvent): Boolean

  fun onSpawn(entity: Entity) {}

  @EventHandler
  fun onEntitySpawn(event: EntitySpawnEvent) {
    val world = event.location.world!!
    if (shouldAttemptReplace(event)) {
      println(this)
      if (Random.nextDouble() < chance) {
        event.setCancelled(true)
        val newEntity = world.spawnEntity(event.location.add(offset), targetEntity)
        onSpawn(newEntity)
      }
    }
  }

}
