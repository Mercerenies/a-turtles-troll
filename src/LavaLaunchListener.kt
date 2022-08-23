
package com.mercerenies.turtletroll

import com.mercerenies.turtletroll.feature.RunnableFeature
import com.mercerenies.turtletroll.feature.container.FeatureContainer
import com.mercerenies.turtletroll.feature.container.ManagerContainer
import com.mercerenies.turtletroll.feature.builder.BuilderState
import com.mercerenies.turtletroll.feature.builder.FeatureContainerFactory

import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.entity.Player
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import org.bukkit.plugin.Plugin

class LavaLaunchManager(_plugin: Plugin) : RunnableFeature(_plugin), Listener {

  companion object : FeatureContainerFactory<FeatureContainer> {

    override fun create(state: BuilderState): FeatureContainer =
      ManagerContainer(LavaLaunchManager(state.plugin))

  }

  private val memory = CooldownMemory<Player>(plugin)

  override val name = "lavalaunch"

  override val description = "Touching lava launches you upward and speeds you up"

  override val taskPeriod: Long = 6L

  @EventHandler
  fun onEntityDamage(event: EntityDamageEvent) {
    if (!isEnabled()) {
      return
    }
    val victim = event.entity
    if (victim is Player) {
      if (event.cause == EntityDamageEvent.DamageCause.LAVA) {
        val time = Constants.TICKS_PER_SECOND * 10
        val velocity = victim.getVelocity().clone()
        velocity.setY(1.3)
        victim.setVelocity(velocity)
        victim.addPotionEffect(PotionEffect(PotionEffectType.SPEED, time, 4))
        memory.add(victim, time.toLong())
      }
    }
  }

  override fun run() {
    if (!isEnabled()) {
      return
    }

    for (player in memory) {
      val loc = player.location
      loc.pitch = 0.0f
      val norm = loc.direction
      val velocity = player.velocity
      velocity.add(norm.multiply(1.3))
      player.velocity = velocity
    }

  }

}
