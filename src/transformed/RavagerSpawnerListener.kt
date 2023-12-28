
package com.mercerenies.turtletroll.transformed

import com.mercerenies.turtletroll.util.component.*
import com.mercerenies.turtletroll.SpawnReason
import com.mercerenies.turtletroll.ReplaceMobsRunnable
import com.mercerenies.turtletroll.feature.container.FeatureContainer
import com.mercerenies.turtletroll.feature.container.ListenerContainer
import com.mercerenies.turtletroll.feature.builder.BuilderState
import com.mercerenies.turtletroll.feature.builder.FeatureContainerFactory

import org.bukkit.event.EventHandler
import org.bukkit.event.entity.CreatureSpawnEvent
import org.bukkit.event.world.ChunkPopulateEvent
import org.bukkit.entity.EntityType
import org.bukkit.entity.Entity
import org.bukkit.Chunk
import org.bukkit.plugin.Plugin

import kotlin.random.Random

class RavagerSpawnerListener(
  val plugin: Plugin,
  override val chance: Double = 0.5,
) : TransformedSpawnerListener() {

  companion object : FeatureContainerFactory<FeatureContainer> {

    override fun create(state: BuilderState): FeatureContainer =
      ListenerContainer(RavagerSpawnerListener(state.plugin, state.config.getDouble("ravagers.probability")))

  }

  override val name = "ravagers"

  override val description = "Allows ravagers to spawn in the Nether"

  override val targetEntity: EntityType = EntityType.RAVAGER

  private inner class HoglinsToRavagers(_chunk: Chunk) : ReplaceMobsRunnable(_chunk) {
    override fun replaceWith(entity: Entity): EntityType? {
      if (entity.type == EntityType.HOGLIN) {
        if (Random.nextDouble() < chance) {
          return EntityType.RAVAGER
        }
      }
      return null
    }
  }

  override fun shouldAttemptReplace(event: CreatureSpawnEvent): Boolean =
    SpawnReason.isNatural(event) && event.entity.type == EntityType.HOGLIN

  @EventHandler
  fun onChunkPopulate(event: ChunkPopulateEvent) {
    if (!isEnabled()) {
      return
    }
    val chunk = event.getChunk()
    HoglinsToRavagers(chunk).schedule(plugin)
  }

}
