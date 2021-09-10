
package com.mercerenies.turtletroll

import org.bukkit.scheduler.BukkitRunnable
import org.bukkit.Chunk
import org.bukkit.entity.Entity
import org.bukkit.entity.EntityType
import org.bukkit.plugin.Plugin

abstract class ReplaceMobsRunnable(val chunk: Chunk) : BukkitRunnable() {

  companion object {
    val DELAY = 3L
  }

  fun schedule(plugin: Plugin) {
    runTaskLater(plugin, DELAY)
  }

  override fun run() {
    val entities = chunk.entities
    for (entity in entities) {
      val replacement_type = replaceWith(entity)
      if (replacement_type != null) {
        val new_entity = entity.location.world!!.spawnEntity(entity.location, replacement_type)
        entity.remove()
        onReplacementMob(new_entity)
      }
    }
  }

  // Return null if the mob shouldn't be replaced
  abstract fun replaceWith(entity: Entity): EntityType?

  open fun onReplacementMob(entity: Entity) {}

}
