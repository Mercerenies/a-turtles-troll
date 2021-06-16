
package com.mercerenies.turtletroll

import com.mercerenies.turtletroll.feature.AbstractFeature
import com.mercerenies.turtletroll.feature.HasEnabledStatus

import org.bukkit.Material
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerMoveEvent
import org.bukkit.entity.Player
import org.bukkit.entity.Boat
import org.bukkit.block.Block
import org.bukkit.block.`data`.Waterlogged
import org.bukkit.scheduler.BukkitRunnable
import org.bukkit.plugin.Plugin

class ElectricWaterListener(
  val plugin: Plugin,
  val pumpkinFeature: HasEnabledStatus,
) : AbstractFeature(), Listener {
  private var memory = CooldownMemory<Player>(plugin)

  companion object {
    val TICKS_PER_SECOND = 20
    val COOLDOWN_TIME = TICKS_PER_SECOND / 3

    fun isWet(block: Block): Boolean {
      if (block.type == Material.WATER) {
        return true
      }
      if ((block is Waterlogged) && (block.isWaterlogged())) {
        return true
      }
      return false
    }

  }

  override val name = "zapwater"

  override val description = "Water electrocutes any players in it"

  @EventHandler
  fun onPlayerMove(event: PlayerMoveEvent) {
    if (!isEnabled()) {
      return
    }
    val block = event.getTo()?.getBlock()
    val player = event.player
    if (shouldStrike(player, block)) {
      strike(player)
    }
  }

  private fun shouldStrike(player: Player, block: Block?): Boolean {
    if (block == null) {
      return false
    }
    if (!isWet(block)) {
      return false
    }
    // Boats provide immunity against the electricity effect
    if (player.getVehicle() is Boat) {
      return false
    }
    // Pumpkins provide immunity against the electricity effect
    if ((player.inventory.helmet?.getType() == Material.CARVED_PUMPKIN) && (pumpkinFeature.isEnabled())){
      return false
    }
    return true
  }

  private fun strike(player: Player) {
    if (!memory.contains(player)) {
      memory.add(player, COOLDOWN_TIME.toLong())
      player.world.strikeLightning(player.location)
    }
  }

}
