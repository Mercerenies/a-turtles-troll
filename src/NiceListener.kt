
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
import org.bukkit.generator.BlockPopulator
import org.bukkit.generator.WorldInfo
import org.bukkit.generator.LimitedRegion
import org.bukkit.event.Listener
import org.bukkit.event.EventHandler
import org.bukkit.event.world.WorldInitEvent

import java.util.Random

class NiceListener(
  val targetHeight: Int = 69,
) : AbstractFeature(), Listener {

  companion object : FeatureContainerFactory<FeatureContainer> {

    val SAFE_BLOCK_TYPES = setOf(
      Material.END_GATEWAY, Material.END_PORTAL,
      Material.END_PORTAL_FRAME, Material.END_CRYSTAL,
    )

    override fun create(state: BuilderState): FeatureContainer {
      return ListenerContainer(NiceListener())
    }

  }

  private val blockPopulator = object : BlockPopulator() {

    override fun populate(
      worldInfo: WorldInfo,
      random: Random,
      chunkX: Int,
      chunkZ: Int,
      limitedRegion: LimitedRegion,
    ) {
      if (!isEnabled()) {
        return
      }

      for (column in BlockSelector.columnsInChunk(chunkX, chunkZ)) {
        limitedRegion.setType(column.x, targetHeight, column.z, Material.AIR)
      }
    }
  }

  override val name = "nice"

  override val description = "Y=69 never has anything generated at that level"

  @EventHandler
  fun onWorldInit(event: WorldInitEvent) {
    // Do it in all worlds :)
    event.world.populators.add(blockPopulator)
  }

}
