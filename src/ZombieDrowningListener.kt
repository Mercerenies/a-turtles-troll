
package com.mercerenies.turtletroll

import com.mercerenies.turtletroll.ext.*
import com.mercerenies.turtletroll.feature.AbstractFeature

import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityTransformEvent
import org.bukkit.entity.Zombie
import org.bukkit.entity.EntityType
import org.bukkit.attribute.Attribute


class ZombieDrowningListener() : AbstractFeature(), Listener {

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
