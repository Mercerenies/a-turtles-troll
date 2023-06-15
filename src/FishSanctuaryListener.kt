
package com.mercerenies.turtletroll

import com.mercerenies.turtletroll.feature.AbstractFeature
import com.mercerenies.turtletroll.feature.container.FeatureContainer
import com.mercerenies.turtletroll.feature.container.ListenerContainer
import com.mercerenies.turtletroll.feature.builder.BuilderState
import com.mercerenies.turtletroll.feature.builder.FeatureContainerFactory

import org.bukkit.Material
import org.bukkit.entity.EntityType
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageEvent

class FishSanctuaryListener() : AbstractFeature(), Listener {

  companion object : FeatureContainerFactory<FeatureContainer> {

    private val FISHES = setOf(
      EntityType.COD,
      EntityType.SALMON,
      EntityType.PUFFERFISH,
      EntityType.TROPICAL_FISH,
    )

    override fun create(state: BuilderState): FeatureContainer =
      ListenerContainer(FishSanctuaryListener())

  }

  override val name = "fishsanctuary"

  override val description = "Fish on dry land spontaneously create water"

  @EventHandler(priority = EventPriority.HIGH)
  fun onEntityDamage(event: EntityDamageEvent) {
    if (!isEnabled()) {
      return
    }
    if (event.isCancelled()) {
      return
    }
    if (FISHES.contains(event.entity.type)) {
      if ((event.cause == EntityDamageEvent.DamageCause.DRYOUT) || (event.cause == EntityDamageEvent.DamageCause.DROWNING)) {
        event.entity.location.block.type = Material.WATER
      }
    }
  }

}
