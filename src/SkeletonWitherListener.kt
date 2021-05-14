
package com.mercerenies.turtletroll

import com.mercerenies.turtletroll.feature.AbstractFeature

import org.bukkit.Material
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.entity.Player
import org.bukkit.entity.Arrow
import org.bukkit.entity.Skeleton
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import org.bukkit.World

class SkeletonWitherListener : AbstractFeature(), Listener {

  companion object {
    val TICKS_PER_SECOND = 20
  }

  override val name = "witherarrow"

  override val description = "Skeleton arrows inflict the Wither effect in the Nether"

  @EventHandler
  fun onEntityDamageByEntity(event: EntityDamageByEntityEvent) {
    if (!isEnabled()) {
      return
    }
    val victim = event.entity
    val damager = event.damager
    if (victim.location.world!!.environment == World.Environment.NETHER) {
      if ((victim is Player) && (damager is Arrow)) {
        val shooter = damager.shooter
        if (shooter is Skeleton) {
          victim.addPotionEffect(PotionEffect(PotionEffectType.WITHER, TICKS_PER_SECOND * 10, 0))
        }
      }
    }
  }

}
