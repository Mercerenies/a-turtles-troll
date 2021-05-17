
package com.mercerenies.turtletroll.falling

import com.mercerenies.turtletroll.feature.Feature

import org.bukkit.scheduler.BukkitRunnable
import org.bukkit.entity.Player
import org.bukkit.plugin.Plugin
import org.bukkit.Location
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.block.Block

class SandAttackRunnable(val targetBlocks: Set<Material>) : FallingObjectRunnable() {

  override val name = "sandattack"

  override val description = "Sand drops on players' heads when standing on certain block types"

  override val maxDropHeight = 15

  override val blockToDrop = Material.SAND

  override val delayTime = TICKS_PER_SECOND.toLong()

  override fun shouldDropOn(player: Player): Boolean {
    val blockUnderneath = player.location.clone().add(0.0, -1.0, 0.0).block.type
    return targetBlocks.contains(blockUnderneath) && super.shouldDropOn(player)
  }

  override fun canDropThroughBlock(block: Block): Boolean = true

}
