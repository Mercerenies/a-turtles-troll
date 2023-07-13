
package com.mercerenies.turtletroll.banish

import org.bukkit.generator.BiomeProvider
import org.bukkit.generator.WorldInfo
import org.bukkit.block.Biome

class ConstantBiomeProvider(
  private val biome: Biome,
) : BiomeProvider() {

  override fun getBiome(worldInfo: WorldInfo, x: Int, y: Int, z: Int): Biome =
    biome

  override fun getBiomes(worldInfo: WorldInfo): List<Biome> =
    listOf(biome)

}
