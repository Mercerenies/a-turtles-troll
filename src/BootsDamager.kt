
package com.mercerenies.turtletroll

import org.bukkit.inventory.meta.Damageable
import org.bukkit.Material
import org.bukkit.entity.Player

import kotlin.random.Random

class BootsDamager(val damageChance: Double = 1.00) {

  // Returns whether the damage was blocked and the player is safe
  fun tryWearDownBoots(player: Player): Boolean {
    val boots = player.inventory.boots
    if (boots == null) {
      return false
    }
    val meta = boots.getItemMeta()
    if (!(meta is Damageable)) {
      return false
    }
    if (Random.nextDouble() <= damageChance) {
      meta.damage += 1
      boots.setItemMeta(meta)
      if (meta.damage >= boots.getType().getMaxDurability()) {
        player.inventory.boots = null
      }
    }
    return true
  }

}
