
package com.mercerenies.turtletroll

import com.mercerenies.turtletroll.feature.AbstractFeature

import org.bukkit.util.Vector
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.entity.EntityType
import org.bukkit.entity.Player
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType

import kotlin.random.Random

class FallDamageListener() : AbstractFeature(), Listener {

  companion object {
    val TICKS_PER_SECOND = 20
  }

  override val name = "fall"

  override val description = "Touching lava launches you upward"

  @EventHandler
  fun onEntityDamage(event: EntityDamageEvent) {
    if (!isEnabled()) {
      return
    }
    val victim = event.entity
    if (victim is Player) {
      if (event.cause == EntityDamageEvent.DamageCause.FALL) {
        victim.addPotionEffect(PotionEffect(PotionEffectType.CONFUSION, TICKS_PER_SECOND * 10, 1))
        victim.addPotionEffect(PotionEffect(PotionEffectType.SLOW, TICKS_PER_SECOND * 10, 0))
      }
    }
  }

}
