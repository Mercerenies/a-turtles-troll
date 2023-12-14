
package com.mercerenies.turtletroll.banish

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
    val minX = chunkX * 16
    val minZ = chunkZ * 16
    for (x in minX..minX + 15) {
      for (z in minZ..minZ + 15) {
        putGrass(limitedRegion, x, lowerGrassHeight, z, Bisected.Half.BOTTOM)
        putGrass(limitedRegion, x, upperGrassHeight, z, Bisected.Half.TOP)
      }
    }
  }

}
