
package com.mercerenies.turtletroll.transformed

import com.mercerenies.turtletroll.ext.*
import com.mercerenies.turtletroll.SpawnReason
import com.mercerenies.turtletroll.ReplaceMobsRunnable

import org.bukkit.event.EventHandler
import org.bukkit.event.entity.CreatureSpawnEvent
import org.bukkit.event.world.ChunkPopulateEvent
import org.bukkit.entity.EntityType
import org.bukkit.entity.Entity
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Fish
import org.bukkit.entity.Drowned
import org.bukkit.Chunk
import org.bukkit.Material
import org.bukkit.plugin.Plugin
import org.bukkit.inventory.ItemStack


class DrownedSpawnerListener(
  val plugin: Plugin,
  override val chance: Double = 1.0,
) : TransformedSpawnerListener() {

  companion object {

    fun addHelmet(entity: LivingEntity) {
      entity.equipment?.helmet = ItemStack(Material.LEATHER_HELMET)
    }

  }

  override val name = "drowned"

  override val description = "Fish are replaced with Drowned"

  override val targetEntity: EntityType = EntityType.DROWNED

  override val cancelOriginal: Boolean = false

  private inner class FishToDrowneds(_chunk: Chunk) : ReplaceMobsRunnable(_chunk) {

    override fun replaceWith(entity: Entity): EntityType? =
      if (entity is Fish) {
        EntityType.DROWNED
      } else {
        null
      }

  }

  override fun shouldAttemptReplace(event: CreatureSpawnEvent): Boolean =
    SpawnReason.isNatural(event) && event.entity is Fish

  @EventHandler
  fun onChunkPopulate(event: ChunkPopulateEvent) {
    if (!isEnabled()) {
      return
    }
    val chunk = event.getChunk()
    FishToDrowneds(chunk).schedule(plugin)
  }

  @EventHandler
  override fun onCreatureSpawn(event: CreatureSpawnEvent) {
    super.onCreatureSpawn(event)

    if (!isEnabled()) {
      return
    }

    // All Drowneds get a helmet :)
    if (event.entity is Drowned) {
      addHelmet(event.entity)
    }

  }

}
