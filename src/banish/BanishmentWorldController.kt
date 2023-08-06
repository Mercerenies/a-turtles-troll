
package com.mercerenies.turtletroll.banish

import org.bukkit.WorldCreator
import org.bukkit.WorldType
import org.bukkit.World
import org.bukkit.Bukkit
import org.bukkit.block.Biome

class BanishmentWorldController {

  companion object {
    val WORLD_NAME = "world_turtle_banishment"

    private val LOWER_GRASS_HEIGHT = -60
    private val UPPER_GRASS_HEIGHT = -59

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

  val world: World by lazy {
    Bukkit.createWorld(creator)!!
  }

  ///// generate tall grass using BlockPopulator here :) (probably WorldInitEvent)

}
