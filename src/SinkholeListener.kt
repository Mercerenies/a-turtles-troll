
package com.mercerenies.turtletroll

import com.mercerenies.turtletroll.feature.AbstractFeature
import com.mercerenies.turtletroll.feature.container.FeatureContainer
import com.mercerenies.turtletroll.feature.container.ListenerContainer
import com.mercerenies.turtletroll.feature.builder.BuilderState
import com.mercerenies.turtletroll.feature.builder.FeatureContainerFactory
import com.mercerenies.turtletroll.ext.*
import com.mercerenies.turtletroll.location.BlockSelector

import org.bukkit.Material
import org.bukkit.World
import org.bukkit.event.Listener
import org.bukkit.event.EventHandler
import org.bukkit.event.world.ChunkPopulateEvent

import kotlin.random.Random

class SinkholeListener(
  val chancePerChunk: Double,
) : AbstractFeature(), Listener {

  companion object : FeatureContainerFactory<FeatureContainer> {

    val SAFE_BLOCK_TYPES = setOf(
      Material.END_GATEWAY, Material.END_PORTAL,
      Material.END_PORTAL_FRAME, Material.END_CRYSTAL,
    )

    override fun create(state: BuilderState): FeatureContainer =
      ListenerContainer(SinkholeListener(state.config.getDouble("sinkhole.chance")))

    private fun obliterateColumn(world: World, x: Int, z: Int) {
      for (y in world.minHeight..world.maxHeight) {
        if (!SAFE_BLOCK_TYPES.contains(world.getType(x, y, z))) {
          world.setType(x, y, z, Material.AIR)
        }
      }
    }

  }

  override val name = "sinkhole"

  override val description = "Sinkholes occasionally generate in random chunks, leading straight down to the void"

  @EventHandler
  fun onChunkPopulate(event: ChunkPopulateEvent) {
    if (!isEnabled()) {
      return
    }
    if (Random.nextDouble() < chancePerChunk) {
      val block = BlockSelector.getRandomBlock(event.chunk)
      obliterateColumn(event.world, block.x, block.z)
    }
  }

}
