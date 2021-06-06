
package com.mercerenies.turtletroll

import com.mercerenies.turtletroll.Weight
import com.mercerenies.turtletroll.sample
import com.mercerenies.turtletroll.feature.AbstractFeature

import org.bukkit.util.Vector
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.entity.EntityType
import org.bukkit.entity.Player

import kotlin.random.Random

class LavaLaunchListener() : AbstractFeature(), Listener {

  override val name = "lavalaunch"

  override val description = "Touching lava launches you upward"

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
      }
    }
  }

}
