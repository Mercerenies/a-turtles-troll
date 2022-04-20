
package com.mercerenies.turtletroll

import com.mercerenies.turtletroll.feature.AbstractFeature

import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.entity.Player
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType

class LavaLaunchListener() : AbstractFeature(), Listener {

  override val name = "lavalaunch"

  override val description = "Touching lava launches you upward and speeds you up"

  @EventHandler
  fun onEntityDamage(event: EntityDamageEvent) {
    if (!isEnabled()) {
      return
    }
    val victim = event.entity
    if (victim is Player) {
      if (event.cause == EntityDamageEvent.DamageCause.LAVA) {
        val velocity = victim.getVelocity().clone()
        velocity.setY(1.3)
        victim.setVelocity(velocity)
        victim.addPotionEffect(PotionEffect(PotionEffectType.SPEED, Constants.TICKS_PER_SECOND * 10, 4))
      }
    }
  }

}
