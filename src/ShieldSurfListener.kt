
package com.mercerenies.turtletroll

import com.mercerenies.turtletroll.feature.AbstractFeature

import org.bukkit.inventory.meta.Damageable
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.entity.Player

class ShieldSurfListener() : AbstractFeature(), Listener {

  companion object {
    val MIN_PITCH = 45

    fun damageShield(player: Player, amount: Double) {
      val shield = player.inventory.getItemInOffHand()
      val meta = shield.getItemMeta()
      if (!(meta is Damageable)) {
        return
      }
      meta.damage += amount.toInt()
      shield.setItemMeta(meta)
      if (meta.damage >= shield.getType().getMaxDurability()) {
        player.inventory.setItemInOffHand(null)
      } else {
        player.inventory.setItemInOffHand(shield)
      }
    }

  }

  override val name = "shieldsurf"

  override val description = "If you look at the ground and hold a shield, you can block fall damage"

  @EventHandler(priority=EventPriority.LOW)
  fun onEntityDamage(event: EntityDamageEvent) {
    if (!isEnabled()) {
      return
    }
    val victim = event.entity
    if (!event.isCancelled()) {
      if (victim is Player) {
        if (event.cause == EntityDamageEvent.DamageCause.FALL) {
          if ((victim.isBlocking()) && (victim.location.pitch >= MIN_PITCH)) {
            event.setCancelled(true)
            damageShield(victim, event.getDamage())
          }
        }
      }
    }
  }

}
