
package com.mercerenies.turtletroll.spillage

import org.bukkit.Material
import org.bukkit.entity.EntityType

object Spillage {

  val defaultHandlers: List<SpillageHandler> = listOf(
    SpillLiquidAndEntity(Material.AXOLOTL_BUCKET, Material.WATER, EntityType.AXOLOTL).exceptInNether(),
    SpillLiquidAndEntity(Material.COD_BUCKET, Material.WATER, EntityType.COD).exceptInNether(),
    SpillLiquidAndEntity(Material.LAVA_BUCKET, Material.LAVA, null),
    SpillLiquidAndEntity(Material.MILK_BUCKET, null, EntityType.COW),
    SpillLiquidAndEntity(Material.POWDER_SNOW_BUCKET, Material.POWDER_SNOW, null),
    SpillLiquidAndEntity(Material.SALMON_BUCKET, Material.WATER, EntityType.SALMON).exceptInNether(),
    SpillLiquidAndEntity(Material.PUFFERFISH_BUCKET, Material.WATER, EntityType.PUFFERFISH).exceptInNether(),
    SpillLiquidAndEntity(Material.TROPICAL_FISH_BUCKET, Material.WATER, EntityType.TROPICAL_FISH).exceptInNether(),
    SpillLiquidAndEntity(Material.WATER_BUCKET, Material.WATER, null).exceptInNether(),
  )

}
