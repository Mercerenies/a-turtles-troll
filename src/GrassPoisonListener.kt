
package com.mercerenies.turtletroll

import org.bukkit.Material
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerMoveEvent
import org.bukkit.entity.Player
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType

class GrassPoisonListener : Listener {

  companion object {
    val TICKS_PER_SECOND = 20
    val BLOCKS = setOf(
      Material.GRASS, Material.TALL_GRASS, Material.FERN,
      Material.LARGE_FERN, Material.DEAD_BUSH
    )
  }

  @EventHandler
  fun onPlayerMove(event: PlayerMoveEvent) {
    val block = event.getTo()?.getBlock()
    if ((block != null) && (BLOCKS.contains(block.type))) {
      val player = event.player
      player.addPotionEffect(PotionEffect(PotionEffectType.POISON, TICKS_PER_SECOND * 5, 0))
      player.addPotionEffect(PotionEffect(PotionEffectType.SLOW, TICKS_PER_SECOND * 10, 1))
    }
  }

}
