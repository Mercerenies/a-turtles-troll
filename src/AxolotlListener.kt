
package com.mercerenies.turtletroll

import com.mercerenies.turtletroll.feature.AbstractFeature
import com.mercerenies.turtletroll.feature.container.FeatureContainer
import com.mercerenies.turtletroll.feature.container.ListenerContainer
import com.mercerenies.turtletroll.feature.builder.BuilderState
import com.mercerenies.turtletroll.feature.builder.FeatureContainerFactory
import com.mercerenies.turtletroll.location.PlayerSelector

import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDeathEvent
import org.bukkit.entity.Axolotl
import org.bukkit.Bukkit

class AxolotlListener() : AbstractFeature(), Listener {

  companion object : FeatureContainerFactory<FeatureContainer> {
    val DISTANCE_SQUARED_LIMIT = 1024.0 // 32 blocks (squared)
    override fun create(state: BuilderState): FeatureContainer = 
      ListenerContainer(AxolotlListener())

  }

  override val name = "axolotl"

  override val description = "When an axolotl dies, the nearest player also dies"

  @EventHandler
  fun onEntityDeath(event: EntityDeathEvent) {
    if (!isEnabled()) {
      return
    }
    val entity = event.entity
    if (entity is Axolotl) {
      val nearestPlayer = PlayerSelector.findNearestPlayer(entity.location, DISTANCE_SQUARED_LIMIT)

      if (nearestPlayer == null) {
        Bukkit.broadcastMessage("RIP an axolotl, who died in peace")
      } else {
        Bukkit.broadcastMessage("RIP an axolotl, who took ${nearestPlayer.getDisplayName()} with them")
        nearestPlayer.damage(9999.0, entity)
      }

    }
  }

}
