
package com.mercerenies.turtletroll.banish

import com.mercerenies.turtletroll.location.BlockSelector

import org.bukkit.Material
import org.bukkit.RegionAccessor
import org.bukkit.block.`data`.Bisected
import org.bukkit.generator.BlockPopulator
import org.bukkit.generator.WorldInfo
import org.bukkit.generator.LimitedRegion

import java.util.Random

class BanishmentBlockPopulator(
  val lowerGrassHeight: Int,
  val upperGrassHeight: Int,
) : BlockPopulator() {

  companion object {

    private fun putGrass(region: RegionAccessor, x: Int, y: Int, z: Int, half: Bisected.Half) {
      region.setType(x, y, z, Material.TALL_GRASS)
      val blockData = region.getBlockData(x, y, z) as Bisected
      blockData.half = half
      region.setBlockData(x, y, z, blockData)
    }

  }

  override fun populate(
    worldInfo: WorldInfo,
    random: Random,
    chunkX: Int,
    chunkZ: Int,
    limitedRegion: LimitedRegion,
  ) {
    for (column in BlockSelector.columnsInChunk(chunkX, chunkZ)) {
      putGrass(limitedRegion, column.x, lowerGrassHeight, column.z, Bisected.Half.BOTTOM)
      putGrass(limitedRegion, column.x, upperGrassHeight, column.z, Bisected.Half.TOP)
    }
  }

}
