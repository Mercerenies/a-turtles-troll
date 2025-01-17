
package com.mercerenies.turtletroll

import com.mercerenies.turtletroll.feature.AbstractFeature
import com.mercerenies.turtletroll.feature.container.FeatureContainer
import com.mercerenies.turtletroll.feature.container.ListenerContainer
import com.mercerenies.turtletroll.feature.builder.BuilderState
import com.mercerenies.turtletroll.feature.builder.FeatureContainerFactory

import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.entity.Player
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType

class FallDamageListener() : AbstractFeature(), Listener {

  companion object : FeatureContainerFactory<FeatureContainer> {

    override fun create(state: BuilderState): FeatureContainer =
      ListenerContainer(FallDamageListener())

  }

  override val name = "fall"

  override val description = "When a player takes fall damage, they are confused and slow for a moment"

  @EventHandler
  fun onEntityDamage(event: EntityDamageEvent) {
    if (!isEnabled()) {
      return
    }
    val victim = event.entity
    if (!event.isCancelled()) {
      if (victim is Player) {
        if (event.cause == EntityDamageEvent.DamageCause.FALL) {
          victim.addPotionEffect(PotionEffect(PotionEffectType.NAUSEA, Constants.TICKS_PER_SECOND * 10, 1))
          victim.addPotionEffect(PotionEffect(PotionEffectType.SLOWNESS, Constants.TICKS_PER_SECOND * 10, 0))
        }
      }
    }
  }

}
