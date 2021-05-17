
package com.mercerenies.turtletroll

import com.mercerenies.turtletroll.feature.AbstractFeature

import org.bukkit.Material
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerMoveEvent
import org.bukkit.entity.Player
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType

class LevitationListener : AbstractFeature(), Listener {

  companion object {
    val TICKS_PER_SECOND = 20
    val BLOCKS = setOf(
      Material.COAL_ORE, Material.IRON_ORE, Material.LAPIS_ORE,
      Material.GOLD_ORE, Material.DIAMOND_ORE, Material.EMERALD_ORE,
      Material.NETHER_QUARTZ_ORE, Material.NETHER_GOLD_ORE, Material.ANCIENT_DEBRIS
    )
  }

  override val name = "levitation"

  override val description = "Certain blocks cause the player to levitate"

  @EventHandler
  fun onPlayerMove(event: PlayerMoveEvent) {
    if (!isEnabled()) {
      return
    }
    val loc = event.getTo()
    if (loc != null) {
      val block = loc.clone().add(0.0, -1.0, 0.0).getBlock()
      if (BLOCKS.contains(block.type)) {
        val player = event.player
        player.addPotionEffect(PotionEffect(PotionEffectType.LEVITATION, TICKS_PER_SECOND * 5, 0))
      }
    }
  }

}
