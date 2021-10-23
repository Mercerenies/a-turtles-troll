
package com.mercerenies.turtletroll

import com.mercerenies.turtletroll.feature.AbstractFeature

import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerMoveEvent
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType

class LevitationListener : AbstractFeature(), Listener {

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
      if (BlockTypes.ORES.contains(block.type)) {
        val player = event.player
        player.addPotionEffect(PotionEffect(PotionEffectType.LEVITATION, Constants.TICKS_PER_SECOND * 5, 0))
      }
    }
  }

}
