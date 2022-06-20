
package com.mercerenies.turtletroll

import com.mercerenies.turtletroll.feature.AbstractFeature
import com.mercerenies.turtletroll.feature.container.FeatureContainer
import com.mercerenies.turtletroll.feature.container.ListenerContainer
import com.mercerenies.turtletroll.feature.builder.BuilderState
import com.mercerenies.turtletroll.feature.builder.FeatureContainerFactory

import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.CreatureSpawnEvent
import org.bukkit.event.world.ChunkPopulateEvent
import org.bukkit.entity.Rabbit
import org.bukkit.entity.EntityType
import org.bukkit.entity.Entity
import org.bukkit.Chunk
import org.bukkit.plugin.Plugin

class KillerRabbitListener(
  val plugin: Plugin,
) : AbstractFeature(), Listener {

  companion object : FeatureContainerFactory<FeatureContainer> {

    override fun create(state: BuilderState): FeatureContainer =
      ListenerContainer(KillerRabbitListener(state.plugin))

  }

  private inner class SpawnKillerRabbit(_chunk: Chunk) : ReplaceMobsRunnable(_chunk) {

    override fun replaceWith(entity: Entity): EntityType? =
      null

    override fun onUnreplacedMob(entity: Entity) {
      super.onUnreplacedMob(entity)
      if (entity is Rabbit) {
        entity.setRabbitType(Rabbit.Type.THE_KILLER_BUNNY)
      }
    }

  }

  override val name = "killerrabbit"

  override val description = "All rabbits are replaced by The Killer Rabbit"

  @EventHandler
  fun onCreatureSpawn(event: CreatureSpawnEvent) {
    if (!isEnabled()) {
      return
    }
    if (!SpawnReason.isNatural(event)) {
      return
    }
    val entity = event.entity
    if (entity is Rabbit) {
      entity.setRabbitType(Rabbit.Type.THE_KILLER_BUNNY)
    }
  }

  @EventHandler
  fun onChunkPopulate(event: ChunkPopulateEvent) {
    if (!isEnabled()) {
      return
    }
    val chunk = event.getChunk()
    SpawnKillerRabbit(chunk).schedule(plugin)
  }

}
