
package com.mercerenies.turtletroll

import com.mercerenies.turtletroll.feature.AbstractFeature

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

class ElectricWaterListener(val plugin: Plugin) : AbstractFeature(), Listener {
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
    if ((block != null) && (isWet(block))) {
      val player = event.player
      // Boats provide immunity against the electricity effect
      if (player.getVehicle() !is Boat) {
        strike(player)
      }
    }
  }

  private fun strike(player: Player) {
    if (!memory.contains(player)) {
      memory.add(player, COOLDOWN_TIME.toLong())
      player.world.strikeLightning(player.location)
    }
  }

}
