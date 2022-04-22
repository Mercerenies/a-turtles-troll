
package com.mercerenies.turtletroll

import com.mercerenies.turtletroll.feature.AbstractFeature
import com.mercerenies.turtletroll.feature.container.FeatureContainer
import com.mercerenies.turtletroll.feature.container.ListenerContainer
import com.mercerenies.turtletroll.feature.builder.BuilderState
import com.mercerenies.turtletroll.feature.builder.FeatureContainerFactory

import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.entity.Player
import org.bukkit.entity.Arrow
import org.bukkit.entity.AbstractSkeleton
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import org.bukkit.World

class SkeletonWitherListener : AbstractFeature(), Listener {

  companion object : FeatureContainerFactory<FeatureContainer> {

    override fun create(state: BuilderState): FeatureContainer =
      ListenerContainer(SkeletonWitherListener())

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
        if (shooter is AbstractSkeleton) {
          victim.addPotionEffect(PotionEffect(PotionEffectType.WITHER, Constants.TICKS_PER_SECOND * 10, 0))
        }
      }
    }
  }

}
