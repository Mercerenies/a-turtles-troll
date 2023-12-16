
package com.mercerenies.turtletroll

import com.mercerenies.turtletroll.feature.AbstractFeature
import com.mercerenies.turtletroll.feature.container.FeatureContainer
import com.mercerenies.turtletroll.feature.container.ListenerContainer
import com.mercerenies.turtletroll.feature.builder.BuilderState
import com.mercerenies.turtletroll.feature.builder.FeatureContainerFactory
import com.mercerenies.turtletroll.ext.*
import com.mercerenies.turtletroll.location.BlockSelector
import com.mercerenies.turtletroll.banish.BanishmentWorldController

import org.bukkit.Material
import org.bukkit.RegionAccessor
import org.bukkit.World
import org.bukkit.generator.BlockPopulator
import org.bukkit.generator.WorldInfo
import org.bukkit.generator.LimitedRegion
import org.bukkit.event.Listener
import org.bukkit.event.EventHandler
import org.bukkit.event.world.WorldInitEvent

import kotlin.math.min

import java.util.Random

class ObsidianWallListener(
  val wallDistance: Int,
) : AbstractFeature(), Listener {

  companion object : FeatureContainerFactory<FeatureContainer> {

    val SAFE_BLOCK_TYPES = setOf(
      Material.END_GATEWAY, Material.END_PORTAL,
      Material.END_PORTAL_FRAME, Material.END_CRYSTAL,
    )

    override fun create(state: BuilderState): FeatureContainer {
      val wallDistance = state.config.getInt("obsidianwall.wall_distance")
      return ListenerContainer(
        ObsidianWallListener(
          wallDistance = wallDistance,
        )
      )
    }

    private fun replaceWithObsidian(worldInfo: WorldInfo, regionAccessor: RegionAccessor, column: BlockSelector.Column) {
      val baseY = findHighestBlock(worldInfo, regionAccessor, column)
      for (y in baseY..min(baseY + 3, worldInfo.maxHeight - 1)) {
        if (!SAFE_BLOCK_TYPES.contains(regionAccessor.getType(column.x, y, column.z))) {
          regionAccessor.setType(column.x, y, column.z, Material.OBSIDIAN)
        }
      }
    }

    private fun findHighestBlock(worldInfo: WorldInfo, regionAccessor: RegionAccessor, column: BlockSelector.Column): Int {
      for (y in worldInfo.maxHeight - 1 downTo worldInfo.minHeight) {
        if (regionAccessor.getType(column.x, y, column.z) != Material.AIR) {
          return y
        }
      }
      return worldInfo.minHeight
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
        if (needsObsidian(column)) {
          replaceWithObsidian(worldInfo, limitedRegion, column)
        }
      }
    }
  }

  override val name = "obsidianwall"

  override val description = "Sinkholes occasionally generate in random chunks, leading straight down to the void"

  @EventHandler
  fun onWorldInit(event: WorldInitEvent) {
    if (event.world.environment != World.Environment.NORMAL) {
      return
    }
    if (event.world.name == BanishmentWorldController.WORLD_NAME) {
      return
    }
    event.world.populators.add(blockPopulator)
  }

  private fun needsObsidian(column: BlockSelector.Column): Boolean =
    (column.x % wallDistance == 0) || (column.z % wallDistance == 0)

}
