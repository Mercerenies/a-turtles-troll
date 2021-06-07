
package com.mercerenies.turtletroll

import com.mercerenies.turtletroll.feature.AbstractFeature

import org.bukkit.Material
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerMoveEvent
import org.bukkit.entity.Player
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import org.bukkit.inventory.meta.Damageable

import kotlin.random.Random

class GrassPoisonListener(
  val bootsDamageChance: Double = 1.00
): AbstractFeature(), Listener {

  companion object {
    val TICKS_PER_SECOND = 20
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
      if (!tryWearDownBoots(player)) {
        player.addPotionEffect(PotionEffect(PotionEffectType.POISON, TICKS_PER_SECOND * 5, 0))
        player.addPotionEffect(PotionEffect(PotionEffectType.SLOW, TICKS_PER_SECOND * 10, 1))
      }
    }
  }

  // Returns whether the damage was blocked and the player is safe
  private fun tryWearDownBoots(player: Player): Boolean {
    val boots = player.inventory.boots
    if (boots == null) {
      return false
    }
    val meta = boots.getItemMeta()
    if (!(meta is Damageable)) {
      return false
    }
    if (Random.nextDouble() <= bootsDamageChance) {
      meta.damage += 1
      boots.setItemMeta(meta)
      if (meta.damage >= boots.getType().getMaxDurability()) {
        player.inventory.boots = null
      }
    }
    return true
  }

}
