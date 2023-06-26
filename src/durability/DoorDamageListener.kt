
package com.mercerenies.turtletroll.durability

import com.mercerenies.turtletroll.feature.container.FeatureContainer
import com.mercerenies.turtletroll.feature.container.ListenerContainer
import com.mercerenies.turtletroll.feature.builder.BuilderState
import com.mercerenies.turtletroll.feature.builder.FeatureContainerFactory

import org.bukkit.Material
import org.bukkit.Location
import org.bukkit.block.Block
import org.bukkit.block.`data`.Bisected

class DoorDamageListener(
  override val maxUses: Int = 16,
) : DurabilityListener() {

  companion object : FeatureContainerFactory<FeatureContainer> {

    val DOUBLE_DOORS = setOf(
      Material.ACACIA_DOOR, Material.BIRCH_DOOR, Material.CRIMSON_DOOR,
      Material.DARK_OAK_DOOR, Material.IRON_DOOR, Material.JUNGLE_DOOR,
      Material.OAK_DOOR, Material.SPRUCE_DOOR, Material.WARPED_DOOR,
    )

    val DOORS = setOf(
      Material.ACACIA_DOOR, Material.ACACIA_TRAPDOOR, Material.BIRCH_DOOR,
      Material.BIRCH_TRAPDOOR, Material.CRIMSON_DOOR, Material.CRIMSON_TRAPDOOR,
      Material.DARK_OAK_DOOR, Material.DARK_OAK_TRAPDOOR, Material.IRON_DOOR,
      Material.IRON_TRAPDOOR, Material.JUNGLE_DOOR, Material.JUNGLE_TRAPDOOR,
      Material.OAK_DOOR, Material.OAK_TRAPDOOR, Material.SPRUCE_DOOR,
      Material.SPRUCE_TRAPDOOR, Material.WARPED_DOOR, Material.WARPED_TRAPDOOR,
    )

    override fun create(state: BuilderState): FeatureContainer =
      ListenerContainer(
        DoorDamageListener(
          maxUses = state.config.getInt("doordrop.max_uses"),
        )
      )

  }

  override val name = "doordrop"

  override val description = "Doors break after some number of uses"

  override fun shouldAffect(block: Block): Boolean =
    DOORS.contains(block.type)

  override fun adjustBlockPosition(block: Block): Location {
    val blockData = block.blockData
    return if ((DOUBLE_DOORS.contains(block.type)) && (blockData is Bisected) && (blockData.getHalf() == Bisected.Half.TOP)) {
      block.location.clone().add(0.0, -1.0, 0.0)
    } else {
      block.location.clone()
    }
  }

}
