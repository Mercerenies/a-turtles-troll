
package com.mercerenies.turtletroll

import com.mercerenies.turtletroll.feature.AbstractFeature
import com.mercerenies.turtletroll.feature.container.FeatureContainer
import com.mercerenies.turtletroll.feature.container.ListenerContainer
import com.mercerenies.turtletroll.feature.builder.BuilderState
import com.mercerenies.turtletroll.feature.builder.FeatureContainerFactory

import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.entity.Player
import org.bukkit.Material
import org.bukkit.block.Block
import org.bukkit.block.BlockFace

class BloodListener() : AbstractFeature(), Listener {

  companion object : FeatureContainerFactory<FeatureContainer> {

    override fun create(state: BuilderState): FeatureContainer =
      ListenerContainer(BloodListener())

    private fun replaceWithRedstone(block: Block) {
      if ((block.type == Material.AIR) && (block.getRelative(BlockFace.DOWN).type != Material.AIR)) {
        block.type = Material.REDSTONE_WIRE
      }
    }

  }

  override val name = "blood"

  override val description = "Taking damage drops placed bluestone dust on the ground"

  @EventHandler
  fun onEntityDamage(event: EntityDamageEvent) {
    if (!isEnabled()) {
      return
    }
    val victim = event.entity
    if (victim is Player) {
      val block = victim.location.block
      replaceWithRedstone(block)
      replaceWithRedstone(block.getRelative(BlockFace.UP))
      replaceWithRedstone(block.getRelative(BlockFace.DOWN))
    }
  }

}
