
package com.mercerenies.turtletroll

import com.mercerenies.turtletroll.feature.AbstractFeature
import com.mercerenies.turtletroll.feature.container.FeatureContainer
import com.mercerenies.turtletroll.feature.container.ListenerContainer
import com.mercerenies.turtletroll.feature.builder.BuilderState
import com.mercerenies.turtletroll.feature.builder.FeatureContainerFactory

import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.ProjectileLaunchEvent
import org.bukkit.entity.EntityType
import org.bukkit.entity.Entity
import org.bukkit.entity.Egg
import org.bukkit.entity.Arrow

import kotlin.random.Random

class EggArrowListener(val chance: Double = 1.0) : AbstractFeature(), Listener {

  companion object : FeatureContainerFactory<FeatureContainer> {
    val SKELETONS = setOf(EntityType.SKELETON, EntityType.STRAY)
    override fun create(state: BuilderState): FeatureContainer = 
      ListenerContainer(EggArrowListener())

  }

  override val name = "eggarrow"

  override val description = "Skeletons will sometimes throw eggs"

  @EventHandler
  fun onProjectileLaunch(event: ProjectileLaunchEvent) {
    if (!isEnabled()) {
      return
    }
    val entity = event.getEntity()
    val source = entity.getShooter()
    if ((entity is Arrow) && (source is Entity) && (SKELETONS.contains(source.getType()))) {
      if (Random.nextDouble() < chance) {
        val egg = entity.world.spawn(entity.location, Egg::class.java)
        egg.velocity = entity.velocity
      }
    }
  }

}
