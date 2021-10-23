
package com.mercerenies.turtletroll

import com.mercerenies.turtletroll.feature.AbstractFeature

import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerMoveEvent
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType

class SlowSlabListener : AbstractFeature(), Listener {

  companion object {
    val BLOCKS = BlockTypes.SLABS union BlockTypes.STAIRS
  }

  private val bootsDamager = BootsDamager()

  override val name = "slowslab"

  override val description = "Half slabs and stairs slow you down"

  @EventHandler
  fun onPlayerMove(event: PlayerMoveEvent) {
    if (!isEnabled()) {
      return
    }
    val block = event.getTo()?.getBlock()
    if ((block != null) && (BLOCKS.contains(block.type))) {
      val player = event.player
      if (!bootsDamager.tryWearDownBoots(player)) {
        player.addPotionEffect(PotionEffect(PotionEffectType.SLOW, Constants.TICKS_PER_SECOND * 10, 3))
      }
    }
  }

}
