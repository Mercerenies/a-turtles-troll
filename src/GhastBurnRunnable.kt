
package com.mercerenies.turtletroll

import com.mercerenies.turtletroll.feature.RunnableFeature
import com.mercerenies.turtletroll.feature.container.FeatureContainer
import com.mercerenies.turtletroll.feature.container.RunnableContainer
import com.mercerenies.turtletroll.feature.builder.BuilderState
import com.mercerenies.turtletroll.feature.builder.FeatureContainerFactory

import org.bukkit.entity.Ghast
import org.bukkit.plugin.Plugin
import org.bukkit.Bukkit
import org.bukkit.World

// TODO This and SilverfishBurnRunnable are very similar...
class GhastBurnRunnable(plugin: Plugin) : RunnableFeature(plugin) {

  companion object : FeatureContainerFactory<FeatureContainer> {

    override fun create(state: BuilderState): FeatureContainer =
      RunnableContainer(GhastBurnRunnable(state.plugin))

  }

  override val name: String = "ghastburn"

  override val description: String = "Ghasts burn in daylight"

  override val taskPeriod = Constants.TICKS_PER_SECOND * 10L

  override fun run() {
    if (!isEnabled()) {
      return
    }
    val overworld = Worlds.getOverworld()
    if (overworld != null) {
      overworld.getEntitiesByClass(Ghast::class.java).forEach { ghast ->
        if (ghast.location.block.getLightFromSky() >= 15) {
          val systemTime = Worlds.getSystemTime()
          if ((systemTime > 0) && (systemTime < 12000)) {
            // Can't set them on fire so we'll just insta-kill them
            ghast.damage(9999.0, null)
          }
        }
      }
    }
  }

}
