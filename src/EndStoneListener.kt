
package com.mercerenies.turtletroll

import com.mercerenies.turtletroll.feature.AbstractFeature

import org.bukkit.Material
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerMoveEvent
import org.bukkit.entity.Player
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType

class EndStoneListener : AbstractFeature(), Listener {

  companion object {
    val TICKS_PER_SECOND = 20
  }

  override val name = "endspeed"

  override val description = "End stones make players move faster"

  @EventHandler
  fun onPlayerMove(event: PlayerMoveEvent) {
    if (!isEnabled()) {
      return
    }
    val loc = event.getTo()
    if (loc != null) {
      val block = loc.clone().add(0.0, -1.0, 0.0).getBlock()
      if (block.type == Material.END_STONE) {
        val player = event.player
        player.addPotionEffect(PotionEffect(PotionEffectType.SPEED, TICKS_PER_SECOND * 6, 4))
      }
    }
  }

}
