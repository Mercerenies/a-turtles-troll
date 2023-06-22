
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
import org.bukkit.entity.Enderman
import org.bukkit.entity.Entity
import org.bukkit.Material

class EndermanGodListener() : AbstractFeature(), Listener {

  companion object : FeatureContainerFactory<FeatureContainer> {

    override fun create(state: BuilderState): FeatureContainer =
      ListenerContainer(EndermanGodListener())

    private fun shouldDamage(attacker: Entity): Boolean {
      if (attacker !is Player) {
        return false
      }
      val weapon = attacker.inventory.getItemInMainHand()
      // Allow netherite hoe, so that the Goddess Hoe event works.
      return (weapon.type == Material.AIR) || (weapon.type == Material.NETHERITE_HOE)
    }

  }

  override val name = "endermangod"

  override val description = "Endermen can only be damaged by your bare fist"

  @EventHandler
  fun onEntityDamageByEntity(event: EntityDamageByEntityEvent) {
    if (!isEnabled()) {
      return
    }
    val victim = event.entity
    val attacker = event.damager
    if (victim is Enderman) {
      if (!shouldDamage(attacker)) {
        event.setCancelled(true)
        return
      }
    }
  }

}
