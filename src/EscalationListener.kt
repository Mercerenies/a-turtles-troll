
package com.mercerenies.turtletroll

import com.mercerenies.turtletroll.feature.AbstractFeature

import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityPotionEffectEvent
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import org.bukkit.entity.Player
import org.bukkit.entity.Entity

class EscalationListener(
  val featureCount: Int = 4,
  val targetEffectType: PotionEffectType = PotionEffectType.LEVITATION
): AbstractFeature(), Listener {

  override val name = "escalation"

  override val description = "If a player has four status effects, they begin to levitate"

  @EventHandler
  fun onEntityPotionEffect(event: EntityPotionEffectEvent) {
    if (!isEnabled()) {
      return
    }

    // Prevent infinite loops since we're going to apply this effect
    // below
    if (event.newEffect?.type == targetEffectType) {
      return
    }

    val entity = event.entity
    if (entity is Player) {
      if (!entity.hasPotionEffect(targetEffectType)) {
        if (entity.getActivePotionEffects().size >= featureCount - 1 && event.action == EntityPotionEffectEvent.Action.ADDED) {
          entity.sendMessage("That escalated quickly...")
          entity.addPotionEffect(PotionEffect(PotionEffectType.LEVITATION, Constants.TICKS_PER_SECOND * 10, 0))
        }
      }
    }

  }

}
