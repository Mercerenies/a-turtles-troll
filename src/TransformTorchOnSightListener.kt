
package com.mercerenies.turtletroll

import com.mercerenies.turtletroll.feature.HasEnabledStatus

import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.block.Block
import org.bukkit.block.`data`.Directional
import org.bukkit.plugin.Plugin

class TransformTorchOnSightListener(
  _plugin: Plugin,
  val pumpkinFeature: HasEnabledStatus,
) : OnSightListener(_plugin) {

  companion object {

    val BLOCKS = setOf(
      Material.REDSTONE_TORCH, Material.REDSTONE_WALL_TORCH, Material.SOUL_TORCH,
      Material.SOUL_WALL_TORCH, Material.TORCH, Material.WALL_TORCH, Material.LANTERN,
      Material.SOUL_LANTERN,
    )

    val WALL_BLOCKS = setOf(
      Material.WALL_TORCH, Material.SOUL_WALL_TORCH, Material.REDSTONE_WALL_TORCH,
    )

  }

  override val name: String = "torches"

  override val description: String = "Torches and other lights transform into redstone torches when you look at them"

  override fun shouldTrigger(player: Player, block: Block): Boolean =
    BLOCKS.contains(block.type)

  override fun performEffect(player: Player, block: Block) {
    if ((Hats.isWearingOrdinaryHat(player)) && (pumpkinFeature.isEnabled())) {
      return
    }

    if (WALL_BLOCKS.contains(block.type)) {
      val facing = (block.blockData as Directional).facing
      block.type = Material.REDSTONE_WALL_TORCH
      val blockData = block.blockData as Directional
      blockData.facing = facing
      block.blockData = blockData
    } else {
      block.type = Material.REDSTONE_TORCH
    }
  }

}
