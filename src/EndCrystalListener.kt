
package com.mercerenies.turtletroll

import com.mercerenies.turtletroll.feature.AbstractFeature
import com.mercerenies.turtletroll.feature.container.FeatureContainer
import com.mercerenies.turtletroll.feature.container.ListenerContainer
import com.mercerenies.turtletroll.feature.builder.BuilderState
import com.mercerenies.turtletroll.feature.builder.FeatureContainerFactory

import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerPortalEvent
import org.bukkit.event.world.ChunkPopulateEvent
import org.bukkit.entity.EnderCrystal
import org.bukkit.entity.EntityType
import org.bukkit.Chunk
import org.bukkit.World
import org.bukkit.Material
import org.bukkit.plugin.Plugin
import org.bukkit.scheduler.BukkitRunnable
import org.bukkit.block.CreatureSpawner


class EndCrystalListener(
  val plugin: Plugin,
) : AbstractFeature(), Listener {

  companion object : FeatureContainerFactory<FeatureContainer> {

    private fun putSpawner(entity: EnderCrystal) {
      val block = entity.getLocation().add(0.0, -1.0, 0.0).block
      block.type = Material.SPAWNER
      val state = block.state as CreatureSpawner
      state.spawnCount = 1
      state.spawnedType = EntityType.BLAZE
      state.update()
    }

    override fun create(state: BuilderState): FeatureContainer = 
      ListenerContainer(EndCrystalListener(state.plugin))

  }

  private class SetupCrystalRunnable(val chunk: Chunk) : BukkitRunnable() {
    override fun run() {
      val entities = chunk.entities
      for (entity in entities) {
        if (entity is EnderCrystal) {
          putSpawner(entity)
        }
      }
    }
  }

  private class RenewCrystalRunnable(val world: World) : BukkitRunnable() {
    override fun run() {
      val entities = world.getEntitiesByClass(EnderCrystal::class.java)
      for (entity in entities) {
        putSpawner(entity)
      }
    }
  }

  override val name = "endcrystal"

  override val description = "End crystals spawn in with Blaze spawners underneath them"

  @EventHandler
  fun onChunkPopulate(event: ChunkPopulateEvent) {
    if (!isEnabled()) {
      return
    }
    val chunk = event.getChunk()
    SetupCrystalRunnable(chunk).runTaskLater(plugin, 10L)
  }

  // Refresh the crystals, just to be sure (useful in worlds where the
  // End was already generated before this feature was added)
  @EventHandler
  fun onPlayerPortal(event: PlayerPortalEvent) {
    if (!isEnabled()) {
      return
    }
    val world = event.getTo()!!.world!!
    RenewCrystalRunnable(world).runTaskLater(plugin, 30L)
  }

}
