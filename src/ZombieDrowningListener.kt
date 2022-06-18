
package com.mercerenies.turtletroll

import com.mercerenies.turtletroll.ext.*
import com.mercerenies.turtletroll.feature.AbstractFeature
import com.mercerenies.turtletroll.feature.container.FeatureContainer
import com.mercerenies.turtletroll.feature.container.ListenerContainer
import com.mercerenies.turtletroll.feature.builder.BuilderState
import com.mercerenies.turtletroll.feature.builder.FeatureContainerFactory

import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityTransformEvent
import org.bukkit.entity.Zombie
import org.bukkit.entity.EntityType

class ZombieDrowningListener() : AbstractFeature(), Listener {

  companion object : FeatureContainerFactory<FeatureContainer> {

    override fun create(state: BuilderState): FeatureContainer =
      ListenerContainer(ZombieDrowningListener())

  }

  override val name = "zombiedrowning"

  override val description = "Zombies turn into giants when they drown, not Drowned"

  @EventHandler
  fun onEntityTransform(event: EntityTransformEvent) {
    if (!isEnabled()) {
      return
    }
    val entity = event.entity
    if ((event.transformReason == EntityTransformEvent.TransformReason.DROWNED) && (entity is Zombie)) {
      val location = entity.location
      event.setCancelled(true)
      entity.damage(99999.0, null)
      location.world!!.spawnEntity(location, EntityType.GIANT)
    }
  }

}
