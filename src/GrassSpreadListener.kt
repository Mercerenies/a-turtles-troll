
package com.mercerenies.turtletroll

import com.mercerenies.turtletroll.feature.AbstractFeature
import com.mercerenies.turtletroll.feature.container.FeatureContainer
import com.mercerenies.turtletroll.feature.container.ListenerContainer
import com.mercerenies.turtletroll.feature.builder.BuilderState
import com.mercerenies.turtletroll.feature.builder.FeatureContainerFactory

import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockSpreadEvent
import org.bukkit.Material

import kotlin.random.Random

class GrassSpreadListener() : AbstractFeature(), Listener {

  companion object : FeatureContainerFactory<FeatureContainer> {

    override fun create(state: BuilderState): FeatureContainer =
      ListenerContainer(GrassSpreadListener())

  }

  override val name = "grassspread"

  override val description = "Grass spreads slower than usual"

  @EventHandler
  fun onBlockSpread(event: BlockSpreadEvent) {
    if (!isEnabled()) {
      return
    }
    if (event.newState.blockData.material == Material.GRASS_BLOCK) {
      // 50% chance of canceling
      if (Random.nextDouble() < 0.5) {
        event.setCancelled(true)
      }
    }
  }

}
