
package com.mercerenies.turtletroll

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

import kotlin.collections.HashSet

class ElectricWaterListener(val plugin: Plugin) : Listener {
  private var memory = HashSet<Player>()

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

  private inner class PlayerCooldown(val player: Player) : BukkitRunnable() {
    override fun run() {
      memory.remove(player)
    }
  }

  @EventHandler
  fun onPlayerMove(event: PlayerMoveEvent) {
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
      memory.add(player)
      player.world.strikeLightning(player.location)
      PlayerCooldown(player).runTaskLater(plugin, COOLDOWN_TIME.toLong())
    }
  }

}
