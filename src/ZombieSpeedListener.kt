
package com.mercerenies.turtletroll

import com.mercerenies.turtletroll.ext.*
import com.mercerenies.turtletroll.feature.AbstractFeature
import com.mercerenies.turtletroll.feature.container.FeatureContainer
import com.mercerenies.turtletroll.feature.container.ListenerContainer
import com.mercerenies.turtletroll.feature.builder.BuilderState
import com.mercerenies.turtletroll.feature.builder.FeatureContainerFactory

import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.CreatureSpawnEvent
import org.bukkit.entity.Zombie
import org.bukkit.attribute.Attribute

class ZombieSpeedListener(
  val desiredSpeed: Double = DEFAULT_SPEED,
) : AbstractFeature(), Listener {

  companion object : FeatureContainerFactory<FeatureContainer> {
    val DEFAULT_SPEED = 0.2738638
    override fun create(state: BuilderState): FeatureContainer =
      ListenerContainer(ZombieSpeedListener())

  }

  override val name = "zombiespeed"

  override val description = "Zombies spawn with altered speed"

  @EventHandler
  fun onCreatureSpawn(event: CreatureSpawnEvent) {
    if (!isEnabled()) {
      return
    }
    val entity = event.entity
    if (entity is Zombie) {
      val instance = entity.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED)
      instance?.setBaseValue(desiredSpeed)
    }
  }

}
