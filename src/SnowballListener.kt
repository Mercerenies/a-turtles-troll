
package com.mercerenies.turtletroll

import com.mercerenies.turtletroll.ext.*
import com.mercerenies.turtletroll.feature.AbstractFeature
import com.mercerenies.turtletroll.feature.container.FeatureContainer
import com.mercerenies.turtletroll.feature.container.ListenerContainer
import com.mercerenies.turtletroll.feature.builder.BuilderState
import com.mercerenies.turtletroll.feature.builder.FeatureContainerFactory

import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.ProjectileHitEvent
import org.bukkit.entity.Snowball
import org.bukkit.entity.Entity
import org.bukkit.entity.Player
import org.bukkit.scheduler.BukkitRunnable
import org.bukkit.plugin.Plugin

class SnowballListener(
  val plugin: Plugin,
  val speedMultiplier: Double = 5.5,
) : AbstractFeature(), Listener {

  companion object : FeatureContainerFactory<FeatureContainer> {

    override fun create(state: BuilderState): FeatureContainer =
      ListenerContainer(SnowballListener(state.plugin))

  }

  class ReverseSpeedRunnable(val target: Entity, val multiplier: Double) : BukkitRunnable() {
    override fun run() {
      target.velocity = target.velocity.multiply(- multiplier)
    }
  }

  override val name: String = "snowball"

  override val description: String = "Snowballs deal reverse knockback"

  @EventHandler
  fun onProjectileHit(event: ProjectileHitEvent) {
    if (!isEnabled()) {
      return
    }

    val projectile = event.entity
    val target = event.hitEntity
    if ((projectile is Snowball) && (target != null) && !(target is Player)) {
      // Note: Players don't suffer knockback from snowballs, so don't
      // schedule this effect for players either.
      ReverseSpeedRunnable(target, speedMultiplier).runTaskLater(plugin, 1L)
    }
  }

}
