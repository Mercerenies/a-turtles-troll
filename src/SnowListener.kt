
package com.mercerenies.turtletroll

import com.mercerenies.turtletroll.feature.AbstractFeature

import org.bukkit.Material
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerMoveEvent
import org.bukkit.entity.Player
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType

class SnowListener : AbstractFeature(), Listener {

  companion object {
    val TICKS_PER_SECOND = 20
  }

  override val name = "snowspeed"

  override val description = "Snow makes players move faster"

  @EventHandler
  fun onPlayerMove(event: PlayerMoveEvent) {
    if (!isEnabled()) {
      return
    }
    val block = event.getTo()?.getBlock()
    if (block?.type == Material.SNOW) {
      val player = event.player
      player.addPotionEffect(PotionEffect(PotionEffectType.SPEED, TICKS_PER_SECOND * 6, 4))
    }
  }

}
