
package com.mercerenies.turtletroll

import com.mercerenies.turtletroll.feature.AbstractFeature
import com.mercerenies.turtletroll.feature.container.FeatureContainer
import com.mercerenies.turtletroll.feature.container.ListenerContainer
import com.mercerenies.turtletroll.feature.builder.BuilderState
import com.mercerenies.turtletroll.feature.builder.FeatureContainerFactory

import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDeathEvent
import org.bukkit.entity.Pillager
import org.bukkit.entity.Slime
import org.bukkit.plugin.Plugin
import org.bukkit.scheduler.BukkitRunnable
import org.bukkit.Location

class PillagerDeathListener(val plugin: Plugin) : AbstractFeature(), Listener {

  companion object : FeatureContainerFactory<FeatureContainer> {

    override fun create(state: BuilderState): FeatureContainer =
      ListenerContainer(PillagerDeathListener(state.plugin))

  }

  private class SummonSlimesRunnable(val location: Location, val count: Int) : BukkitRunnable() {
    override fun run() {
      for (i in 1..count) {
        val entity = location.world!!.spawn(location, Slime::class.java)
        entity.setSize(2)
      }
    }
  }

  override val name = "pillagerdeath"

  override val description = "Killing a pillager summons slimes"

  @EventHandler
  fun onEntityDeath(event: EntityDeathEvent) {
    if (!isEnabled()) {
      return
    }
    val entity = event.entity
    if (entity is Pillager) {
      val location = entity.location
      SummonSlimesRunnable(location, 3).runTaskLater(plugin, 2L)
    }
  }

}
