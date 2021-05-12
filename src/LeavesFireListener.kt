
package com.mercerenies.turtletroll

import org.bukkit.Material
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerMoveEvent
import org.bukkit.entity.Player
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import org.bukkit.Location
import org.bukkit.scheduler.BukkitRunnable
import org.bukkit.plugin.Plugin

import kotlin.collections.HashSet

class LeavesFireListener(val plugin: Plugin) : Listener {
  private var memory = HashSet<Location>()

  companion object {
    val TICKS_PER_SECOND = 20
    val DELAY = TICKS_PER_SECOND
    val BLOCKS = setOf(
      Material.ACACIA_LEAVES, Material.BIRCH_LEAVES, Material.DARK_OAK_LEAVES,
      Material.OAK_LEAVES, Material.JUNGLE_LEAVES, Material.SPRUCE_LEAVES,
    )
  }

  private inner class DelayedFire(val location: Location) : BukkitRunnable() {
    override fun run() {
      memory.remove(location)
      location.block.type = Material.FIRE
    }
  }

  @EventHandler
  fun onPlayerMove(event: PlayerMoveEvent) {
    val loc = event.getTo()
    if (loc != null) {
      val block = loc.clone().add(0.0, -1.0, 0.0).getBlock()
      if (BLOCKS.contains(block.type)) {
        if (!memory.contains(loc)) {
          memory.add(loc)
          DelayedFire(loc).runTaskLater(plugin, DELAY.toLong())
        }
      }
    }
  }

}