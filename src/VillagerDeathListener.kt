
package com.mercerenies.turtletroll

import com.mercerenies.turtletroll.feature.AbstractFeature
import com.mercerenies.turtletroll.feature.container.FeatureContainer
import com.mercerenies.turtletroll.feature.container.ListenerContainer
import com.mercerenies.turtletroll.feature.builder.BuilderState
import com.mercerenies.turtletroll.feature.builder.FeatureContainerFactory

import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDeathEvent
import org.bukkit.entity.EntityType
import org.bukkit.inventory.ItemStack
import org.bukkit.Material

class VillagerDeathListener(
  private val leatherCount: Int,
) : AbstractFeature(), Listener {

  companion object : FeatureContainerFactory<FeatureContainer> {

    val AFFECTED_ENTITY_TYPES = setOf(
      EntityType.VILLAGER, EntityType.ZOMBIE_VILLAGER, EntityType.WANDERING_TRADER,
    )

    override fun create(state: BuilderState): FeatureContainer =
      ListenerContainer(VillagerDeathListener(state.config.getInt("villagerdeath.leather_count")))

  }

  override val name = "villagerdeath"

  override val description = "Villagers drop leather"

  @EventHandler
  fun onEntityDeath(event: EntityDeathEvent) {
    if (!isEnabled()) {
      return
    }
    val entity = event.entity
    if (AFFECTED_ENTITY_TYPES.contains(entity.getType())) {
      event.getDrops().add(ItemStack(Material.LEATHER, leatherCount))
    }
  }

}
