
package com.mercerenies.turtletroll.transformed

import com.mercerenies.turtletroll.util.component.*
import com.mercerenies.turtletroll.feature.AbstractFeature

import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.CreatureSpawnEvent
import org.bukkit.entity.Entity
import org.bukkit.entity.EntityType
import org.bukkit.util.Vector

import kotlin.random.Random

abstract class TransformedSpawnerListener() : AbstractFeature(), Listener {

  abstract val chance: Double

  abstract val targetEntity: EntityType

  open val cancelOriginal: Boolean
    get() = true

  open val offset: Vector
    get() = Vector(0.0, 0.0, 0.0)

  abstract fun shouldAttemptReplace(event: CreatureSpawnEvent): Boolean

  open fun onSpawn(entity: Entity) {}

  @EventHandler
  open fun onCreatureSpawn(event: CreatureSpawnEvent) {
    if (!isEnabled()) {
      return
    }
    val world = event.location.world!!
    if (shouldAttemptReplace(event)) {
      if (Random.nextDouble() < chance) {
        if (cancelOriginal) {
          event.setCancelled(true)
        }
        val newEntity = world.spawnEntity(event.location.add(offset), targetEntity)
        onSpawn(newEntity)
      }
    }
  }

}
