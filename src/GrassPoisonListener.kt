
package com.mercerenies.turtletroll

import com.mercerenies.turtletroll.feature.AbstractFeature

import org.bukkit.Material
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerMoveEvent
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType


class GrassPoisonListener(
  _bootsDamageChance: Double = 1.00
): AbstractFeature(), Listener {
  private val bootsDamager = BootsDamager(_bootsDamageChance)

  companion object {
    val BLOCKS = setOf(
      Material.GRASS, Material.TALL_GRASS, Material.FERN,
      Material.LARGE_FERN, Material.DEAD_BUSH, Material.CRIMSON_ROOTS, Material.WARPED_ROOTS
    )
  }

  override val name = "tallgrass"

  override val description = "Tall grass poisons and slows its victims"

  @EventHandler
  fun onPlayerMove(event: PlayerMoveEvent) {
    if (!isEnabled()) {
      return
    }
    val block = event.getTo()?.getBlock()
    if ((block != null) && (BLOCKS.contains(block.type))) {
      val player = event.player
      if (!bootsDamager.tryWearDownBoots(player)) {
        player.addPotionEffect(PotionEffect(PotionEffectType.POISON, Constants.TICKS_PER_SECOND * 5, 0))
        player.addPotionEffect(PotionEffect(PotionEffectType.SLOW, Constants.TICKS_PER_SECOND * 10, 1))
      }
    }
  }

}
