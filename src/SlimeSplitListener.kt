
package com.mercerenies.turtletroll

import com.mercerenies.turtletroll.feature.AbstractFeature
import com.mercerenies.turtletroll.feature.container.FeatureContainer
import com.mercerenies.turtletroll.feature.container.ListenerContainer
import com.mercerenies.turtletroll.feature.builder.BuilderState
import com.mercerenies.turtletroll.feature.builder.FeatureContainerFactory

import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.CreatureSpawnEvent
import org.bukkit.entity.Slime

import kotlin.math.min

class SlimeSplitListener(
  _maxSlimeSize: Int,
) : AbstractFeature(), Listener {

  companion object : FeatureContainerFactory<FeatureContainer> {

    override fun create(state: BuilderState): FeatureContainer =
      ListenerContainer(SlimeSplitListener(state.config.getInt("slimesplit.max_slime_size")))

  }

  private val maxSlimeSize = min(_maxSlimeSize, 15)

  override val name = "slimesplit"

  override val description = "When a slime dies, it splits into larger slimes instead of smaller ones"

  @EventHandler
  fun onCreatureSpawn(event: CreatureSpawnEvent) {
    if (!isEnabled()) {
      return
    }

    val entity = event.getEntity()
    if (entity is Slime) {

      // If the slime was spawned because of a larger slime splitting,
      // change its size appropriately.
      if (event.spawnReason == CreatureSpawnEvent.SpawnReason.SLIME_SPLIT) {
        // Internally, slime sizes are 0-indexed, but the game's code
        // treats it as 1-indexed. When a slime dies, its children
        // have (1-indexed) size equal to half of its own (1-indexed)
        // size, rounded down. We want the parent's size, which (due
        // to rounding), we can't get with certainty. So this is the
        // size of the largest parent that could have spawned this
        // slime. We make the child one size larger than that, unless
        // the child would be larger than the maximum (0-indexed)
        val parentSize = entity.size * 2 + 1
        val childSize = parentSize + 1
        if (childSize > maxSlimeSize) {
          event.setCancelled(true)
        } else {
          entity.size = childSize
        }
      } else {
        // Slimes spawned for other reasons should have a minimum size
        // of 2.
        if (entity.size < 2) {
          entity.size = 2
        }
      }

    }

  }

}
