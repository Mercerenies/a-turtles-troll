
package com.mercerenies.turtletroll

import com.mercerenies.turtletroll.feature.AbstractFeature
import com.mercerenies.turtletroll.feature.container.FeatureContainer
import com.mercerenies.turtletroll.feature.container.ListenerContainer
import com.mercerenies.turtletroll.feature.builder.BuilderState
import com.mercerenies.turtletroll.feature.builder.FeatureContainerFactory

import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDeathEvent
import org.bukkit.entity.Creeper
import org.bukkit.inventory.ItemStack
import org.bukkit.Material


class ChargedCreeperListener() : AbstractFeature(), Listener {

  companion object : FeatureContainerFactory<FeatureContainer> {

    override fun create(state: BuilderState): FeatureContainer = 
      ListenerContainer(ChargedCreeperListener())

  }

  override val name = "chargedcreeper"

  override val description = "Killing a charged creeper drops a diamond"

  @EventHandler
  fun onEntityDeath(event: EntityDeathEvent) {
    if (!isEnabled()) {
      return
    }
    val entity = event.entity
    if (entity is Creeper) {
      if (entity.isPowered()) {
        event.getDrops().add(ItemStack(Material.DIAMOND))
      }
    }
  }

}
