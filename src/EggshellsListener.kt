
package com.mercerenies.turtletroll

import com.mercerenies.turtletroll.feature.AbstractFeature
import com.mercerenies.turtletroll.feature.container.FeatureContainer
import com.mercerenies.turtletroll.feature.container.ListenerContainer
import com.mercerenies.turtletroll.feature.builder.BuilderState
import com.mercerenies.turtletroll.feature.builder.FeatureContainerFactory

import org.bukkit.World
import org.bukkit.Material
import org.bukkit.util.Vector
import org.bukkit.block.Block
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerMoveEvent

class EggshellsListener() : AbstractFeature(), Listener {

  companion object : FeatureContainerFactory<FeatureContainer> {

    fun isSolidOrLava(block: Block): Boolean {
      if (block.type == Material.LAVA) {
        // Lava is valid
        return true
      }
      if (BlockTypes.STAIRS.contains(block.type) || BlockTypes.SLABS.contains(block.type)) {
        // Stairs and slabs are half-height and are invalid
        return false
      }
      // Otherwise, check that it's solid and occluding
      return block.type.isSolid() && block.type.isOccluding()
    }

    fun shouldTurnToLava(block: Block): Boolean {
      // See if it's surrounded
      val relLocations = listOf(
        Vector(1, 0, 0), Vector(-1, 0, 0),
        Vector(0, 1, 0), Vector(0, -1, 0),
        Vector(0, 0, 1), Vector(0, 0, -1),
      )
      for (relLoc in relLocations) {
        val loc = block.location.add(relLoc)
        if (!isSolidOrLava(loc.block)) {
          return false
        }
      }
      // If the block above it has gravity, don't transform
      if (block.location.add(0.0, 1.0, 0.0).block.type.hasGravity()) {
        return false
      }
      return true
    }

    fun tryTurnToLava(block: Block) {
      if (shouldTurnToLava(block)) {
        block.type = Material.LAVA
      }
    }

    override fun create(state: BuilderState): FeatureContainer =
      ListenerContainer(EggshellsListener())

  }

  override val name = "eggshells"

  override val description = "The block two below the player turns to lava if surrounded"

  @EventHandler
  fun onPlayerMove(event: PlayerMoveEvent) {
    if (!isEnabled()) {
      return
    }
    val playerBlock = event.getTo().getBlock()
    val underBlock = playerBlock.location.add(0.0, -2.0, 0.0).block
    // I might turn this on the Nether later if I change my mind.
    // Definitely don't want it in the End though.
    if ((event.player.location.world?.environment == World.Environment.NORMAL)) {
      tryTurnToLava(underBlock)
    }
  }

}
