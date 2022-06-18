
package com.mercerenies.turtletroll

import com.mercerenies.turtletroll.feature.AbstractFeature
import com.mercerenies.turtletroll.feature.container.FeatureContainer
import com.mercerenies.turtletroll.feature.container.ListenerContainer
import com.mercerenies.turtletroll.feature.builder.BuilderState
import com.mercerenies.turtletroll.feature.builder.FeatureContainerFactory

import org.bukkit.inventory.meta.Damageable
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.event.entity.EntityCombustByBlockEvent
import org.bukkit.entity.Player
import org.bukkit.Material

class ShieldSurfListener() : AbstractFeature(), Listener {

  companion object : FeatureContainerFactory<FeatureContainer> {
    val MIN_PITCH = 45

    fun isShieldingDownward(player: Player): Boolean =
      player.isBlocking() && player.location.pitch >= MIN_PITCH

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

    override fun create(state: BuilderState): FeatureContainer =
      ListenerContainer(ShieldSurfListener())

  }

  override val name = "shieldsurf"

  override val description = "If you look at the ground and hold a shield, you can block fall damage and lava damage"

  @EventHandler(priority = EventPriority.LOW)
  fun onEntityDamage(event: EntityDamageEvent) {
    if (!isEnabled()) {
      return
    }
    val victim = event.entity
    if (!event.isCancelled()) {
      if (victim is Player) {
        if ((event.cause == EntityDamageEvent.DamageCause.FALL) || (event.cause == EntityDamageEvent.DamageCause.LAVA)) {
          if (isShieldingDownward(victim)) {
            event.setCancelled(true)
            damageShield(victim, event.getDamage())
          }
        }
      }
    }
  }

  @EventHandler(priority = EventPriority.LOW)
  fun onEntityCombustByBlock(event: EntityCombustByBlockEvent) {
    if (!isEnabled()) {
      return
    }
    val victim = event.entity
    if (!event.isCancelled()) {
      if (victim is Player) {
        if (event.combuster?.type == Material.LAVA) {
          if (isShieldingDownward(victim)) {
            event.setCancelled(true)
            damageShield(victim, 1.0)
          }
        }
      }
    }
  }

}
