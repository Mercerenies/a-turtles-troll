
package com.mercerenies.turtletroll

import com.mercerenies.turtletroll.feature.AbstractFeature

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

class ForestFireListener(val plugin: Plugin) : AbstractFeature(), Listener {
  private var memory = HashSet<Location>()

  override val name = "forestfire"

  override val description = "Leaves and ice catch fire when you walk on them"

  companion object {
    val TICKS_PER_SECOND = 20
    val DELAY = TICKS_PER_SECOND
    val BLOCKS = setOf(
      Material.ACACIA_LEAVES, Material.AZALEA_LEAVES, Material.BIRCH_LEAVES, Material.DARK_OAK_LEAVES,
      Material.FLOWERING_AZALEA_LEAVES, Material.OAK_LEAVES, Material.JUNGLE_LEAVES, Material.SPRUCE_LEAVES,
      Material.BLUE_ICE, Material.FROSTED_ICE, Material.ICE, Material.PACKED_ICE,
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
    if (!isEnabled()) {
      return
    }
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
