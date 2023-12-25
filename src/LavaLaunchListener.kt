
package com.mercerenies.turtletroll

import com.mercerenies.turtletroll.feature.AbstractFeature
import com.mercerenies.turtletroll.feature.container.FeatureContainer
import com.mercerenies.turtletroll.feature.container.ListenerContainer
import com.mercerenies.turtletroll.feature.container.withPlayerDebugCommand
import com.mercerenies.turtletroll.feature.builder.BuilderState
import com.mercerenies.turtletroll.feature.builder.FeatureContainerFactory

import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.entity.Player
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType

class LavaLaunchListener(
  private val velocityY: Double,
) : AbstractFeature(), Listener {

  companion object : FeatureContainerFactory<FeatureContainer> {

    override fun create(state: BuilderState): FeatureContainer {
      val listener = LavaLaunchListener(state.config.getDouble("lavalaunch.velocity_y"))
      return ListenerContainer(listener).withPlayerDebugCommand("lavalaunch") { player ->
        listener.performLaunch(player)
        true
      }
    }

  }

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
        performLaunch(victim)
      }
    }
  }

  private fun performLaunch(player: Player) {
    val velocity = player.getVelocity().clone()
    velocity.setY(velocityY)
    player.setVelocity(velocity)
    player.addPotionEffect(PotionEffect(PotionEffectType.SPEED, Constants.TICKS_PER_SECOND * 10, 4))
  }

}
