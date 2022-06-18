
package com.mercerenies.turtletroll

import com.mercerenies.turtletroll.feature.AbstractFeature
import com.mercerenies.turtletroll.feature.container.FeatureContainer
import com.mercerenies.turtletroll.feature.container.ListenerContainer
import com.mercerenies.turtletroll.feature.builder.BuilderState
import com.mercerenies.turtletroll.feature.builder.FeatureContainerFactory
import com.mercerenies.turtletroll.ext.*

import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.ProjectileLaunchEvent
import org.bukkit.entity.Witch
import org.bukkit.entity.Parrot
import org.bukkit.plugin.Plugin

import kotlin.random.Random

class WitchAttackListener(
  val plugin: Plugin,
  val chance: Double = 0.5,
) : AbstractFeature(), Listener {

  companion object : FeatureContainerFactory<FeatureContainer> {
    val COOLDOWN_TIME = Constants.TICKS_PER_SECOND * 10

    override fun create(state: BuilderState): FeatureContainer =
      ListenerContainer(WitchAttackListener(state.plugin))

  }

  override val name = "witchattack"

  override val description = "Witches will sometimes throw parrots instead of potions"

  @EventHandler
  fun onProjectileLaunch(event: ProjectileLaunchEvent) {
    if (!isEnabled()) {
      return
    }
    val projectile = event.entity
    val source = projectile.shooter
    if (source is Witch) {
      if (Random.nextDouble() < chance) {
        event.setCancelled(true)
        val parrot = projectile.location.world!!.spawn(projectile.location, Parrot::class.java)
        parrot.variant = Parrot.Variant.values().toList().sample()!!
      }
    }
  }

}
