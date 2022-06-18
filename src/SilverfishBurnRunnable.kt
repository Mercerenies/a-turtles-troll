
package com.mercerenies.turtletroll

import com.mercerenies.turtletroll.feature.RunnableFeature
import com.mercerenies.turtletroll.feature.container.FeatureContainer
import com.mercerenies.turtletroll.feature.container.RunnableContainer
import com.mercerenies.turtletroll.feature.builder.BuilderState
import com.mercerenies.turtletroll.feature.builder.FeatureContainerFactory

import org.bukkit.entity.Silverfish
import org.bukkit.plugin.Plugin

class SilverfishBurnRunnable(
  plugin: Plugin,
) : RunnableFeature(plugin) {

  companion object : FeatureContainerFactory<FeatureContainer> {

    override fun create(state: BuilderState): FeatureContainer =
      RunnableContainer(SilverfishBurnRunnable(state.plugin))

  }

  override val name = "silverfishburn"

  override val description = "Silverfish burn in daylight"

  override val taskPeriod = 8L

  override fun run() {
    if (!isEnabled()) {
      return
    }
    val overworld = Worlds.getOverworld()
    if (overworld != null) {
      overworld.getEntitiesByClass(Silverfish::class.java).forEach { silverfish ->
        if (silverfish.location.block.getLightFromSky() >= 15) {
          val systemTime = Worlds.getSystemTime()
          if ((systemTime > 0) && (systemTime < 12000)) {
            silverfish.setFireTicks(Constants.TICKS_PER_SECOND * 5)
          }
        }
      }
    }
  }

}
