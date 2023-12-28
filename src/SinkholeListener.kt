
package com.mercerenies.turtletroll

import com.mercerenies.turtletroll.config.CheckedConfigOptions
import com.mercerenies.turtletroll.feature.AbstractFeature
import com.mercerenies.turtletroll.feature.container.FeatureContainer
import com.mercerenies.turtletroll.feature.container.ListenerContainer
import com.mercerenies.turtletroll.feature.builder.BuilderState
import com.mercerenies.turtletroll.feature.builder.FeatureContainerFactory
import com.mercerenies.turtletroll.util.component.*
import com.mercerenies.turtletroll.location.BlockSelector

import org.bukkit.Material
import org.bukkit.World
import org.bukkit.event.Listener
import org.bukkit.event.EventHandler
import org.bukkit.event.world.ChunkPopulateEvent

import kotlin.random.Random

class SinkholeListener(
  val chancePerChunk: Map<World.Environment, Double>,
) : AbstractFeature(), Listener {

  companion object : FeatureContainerFactory<FeatureContainer> {

    val SAFE_BLOCK_TYPES = setOf(
      Material.END_GATEWAY, Material.END_PORTAL,
      Material.END_PORTAL_FRAME, Material.END_CRYSTAL,
    )

    override fun create(state: BuilderState): FeatureContainer =
      ListenerContainer(SinkholeListener(loadWorldProbabilityMap(state.config)))

    private fun obliterateColumn(world: World, column: BlockSelector.Column) {
      for (y in world.minHeight..world.maxHeight) {
        if (!SAFE_BLOCK_TYPES.contains(world.getType(column.x, y, column.z))) {
          world.setType(column.x, y, column.z, Material.AIR)
        }
      }
    }

    private fun loadWorldProbabilityMap(config: CheckedConfigOptions): Map<World.Environment, Double> =
      mapOf(
        World.Environment.NORMAL to config.getDouble("sinkhole.normal_chance"),
        World.Environment.NETHER to config.getDouble("sinkhole.nether_chance"),
        World.Environment.THE_END to config.getDouble("sinkhole.end_chance"),
      )

  }

  override val name = "sinkhole"

  override val description = "Sinkholes occasionally generate in random chunks, leading straight down to the void"

  @EventHandler
  fun onChunkPopulate(event: ChunkPopulateEvent) {
    if (!isEnabled()) {
      return
    }
    val probability = chancePerChunk[event.world.environment] ?: 0.0
    if (Random.nextDouble() < probability) {
      obliterateColumn(event.world, BlockSelector.getRandomColumn(event.chunk))
    }
  }

}
