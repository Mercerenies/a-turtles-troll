
package com.mercerenies.turtletroll

import com.mercerenies.turtletroll.feature.AbstractFeature
import com.mercerenies.turtletroll.feature.container.FeatureContainer
import com.mercerenies.turtletroll.feature.container.ListenerContainer
import com.mercerenies.turtletroll.feature.builder.BuilderState
import com.mercerenies.turtletroll.feature.builder.FeatureContainerFactory

import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockPlaceEvent
import org.bukkit.entity.EntityType
import org.bukkit.Material
import org.bukkit.World

import kotlin.random.Random

class WhiteDirtListener(
    private val changeChance: Double,
) : AbstractFeature(), Listener {

  companion object : FeatureContainerFactory<FeatureContainer> {
    override fun create(state: BuilderState): FeatureContainer {
      val chance = state.config.getDouble("whitedirt.chance")
      return ListenerContainer(WhiteDirtListener(chance))
    }
  }

  override val name: String = "whitedirt"

  override val description: String = "Dirt has a small chance of becoming sand when placed"

  @EventHandler
  fun onBlockPlace(event: BlockPlaceEvent) {
    if (!isEnabled()) {
      return
    }
    val targetBlock = event.getBlockPlaced()
    val world = targetBlock.world
    if ((Random.nextDouble() < changeChance) && (targetBlock.type == Material.DIRT)) {
      targetBlock.type = Material.SAND
      if (Random.nextDouble() < 0.1) {
        targetBlock.type = Material.RED_SAND
      }
    }
  }

}
