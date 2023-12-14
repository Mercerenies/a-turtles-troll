
package com.mercerenies.turtletroll.banish

import org.bukkit.WorldCreator
import org.bukkit.WorldType
import org.bukkit.World
import org.bukkit.Bukkit
import org.bukkit.block.Biome
import org.bukkit.generator.BlockPopulator

class BanishmentWorldController {

  companion object {
    val WORLD_NAME = "world_turtle_banishment"

    val LOWER_GRASS_HEIGHT = -60
    val UPPER_GRASS_HEIGHT = -59

    private val generatorSettings =
      GeneratorSettings(
        biome = "ocean",
        layers = listOf(
          GeneratorSettings.Layer("bedrock", 1),
          GeneratorSettings.Layer("stone", 2),
          GeneratorSettings.Layer("dirt", 1),
          GeneratorSettings.Layer("grass_block", 1),
        ),
      )

  }

  private val creator: WorldCreator =
    WorldCreator(WORLD_NAME)
      .biomeProvider(ConstantBiomeProvider(Biome.OCEAN))
      .environment(World.Environment.NORMAL)
      .generateStructures(false)
      .generatorSettings(generatorSettings.toJsonString())
      .type(WorldType.FLAT)

  val blockPopulator: BlockPopulator =
    BanishmentBlockPopulator(
      lowerGrassHeight = LOWER_GRASS_HEIGHT,
      upperGrassHeight = UPPER_GRASS_HEIGHT,
    )

  val world: World by lazy {
    Bukkit.createWorld(creator)!!
  }

}
