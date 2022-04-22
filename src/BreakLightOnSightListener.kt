
package com.mercerenies.turtletroll

import com.mercerenies.turtletroll.feature.HasEnabledStatus
import com.mercerenies.turtletroll.feature.container.FeatureContainer
import com.mercerenies.turtletroll.feature.container.ListenerContainer
import com.mercerenies.turtletroll.feature.builder.BuilderState
import com.mercerenies.turtletroll.feature.builder.FeatureContainerFactory

import org.bukkit.Material
import org.bukkit.block.Block
import org.bukkit.plugin.Plugin
import org.bukkit.entity.Player

class BreakLightOnSightListener(
  _plugin: Plugin,
  val pumpkinFeature: HasEnabledStatus,
) : BreakOnSightListener(_plugin) {

  companion object {
    val BLOCKS = setOf(
      Material.REDSTONE_TORCH, Material.REDSTONE_WALL_TORCH, Material.SOUL_TORCH,
      Material.SOUL_WALL_TORCH, Material.TORCH, Material.WALL_TORCH, Material.LANTERN,
      Material.SOUL_LANTERN,
    )

  }

  override val name: String = "torches"

  override val description: String = "Torches and similar light sources break when you look at them"

  override fun shouldTrigger(player: Player, block: Block): Boolean {
    if ((player.inventory.helmet?.getType() == Material.CARVED_PUMPKIN) && (pumpkinFeature.isEnabled())) {
      return false
    }
    return BLOCKS.contains(block.type)
  }

}
