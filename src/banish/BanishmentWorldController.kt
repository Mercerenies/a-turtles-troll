
package com.mercerenies.turtletroll.banish

import org.bukkit.WorldCreator
import org.bukkit.WorldType
import org.bukkit.World
import org.bukkit.Bukkit
import org.bukkit.block.Biome

class BanishmentWorldController {

  companion object {
    val WORLD_NAME = "turtle_banishment"

    private val generatorSettings =
      GeneratorSettings(
        biome = "swap",
        layers = listOf(
          GeneratorSettings.Layer("bedrock", 1),
          GeneratorSettings.Layer("stone", 2),
          GeneratorSettings.Layer("dirt", 1),
          GeneratorSettings.Layer("grass", 1),
        ),
      )

  }

  private val creator: WorldCreator =
    WorldCreator(WORLD_NAME)
      .biomeProvider(ConstantBiomeProvider(Biome.SWAMP))
      .environment(World.Environment.CUSTOM)
      .generateStructures(false)
      .generatorSettings(generatorSettings.toJsonString())
      .type(WorldType.FLAT)

  val world: World by lazy {
    Bukkit.createWorld(creator)!!
  }

}
