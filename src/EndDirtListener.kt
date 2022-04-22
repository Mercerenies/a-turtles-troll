
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

class EndDirtListener() : AbstractFeature(), Listener {

  companion object : FeatureContainerFactory<FeatureContainer> {

    override fun create(state: BuilderState): FeatureContainer = 
      ListenerContainer(EndDirtListener())

  }

  override val name: String = "enddirt"

  override val description: String = "Dirt placed in the End turns to a Shulker"

  @EventHandler
  fun onBlockPlace(event: BlockPlaceEvent) {
    if (!isEnabled()) {
      return
    }
    val targetBlock = event.getBlockPlaced()
    val world = targetBlock.world
    if (world.environment == World.Environment.THE_END) {
      if (targetBlock.type == Material.DIRT) {
        targetBlock.type = Material.AIR
        world.spawnEntity(targetBlock.location, EntityType.SHULKER)
      }
    }
  }

}
