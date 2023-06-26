
package com.mercerenies.turtletroll

import com.mercerenies.turtletroll.feature.AbstractFeature
import com.mercerenies.turtletroll.feature.container.FeatureContainer
import com.mercerenies.turtletroll.feature.container.ListenerContainer
import com.mercerenies.turtletroll.feature.builder.BuilderState
import com.mercerenies.turtletroll.feature.builder.FeatureContainerFactory

import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDeathEvent
import org.bukkit.event.entity.EntityExplodeEvent
import org.bukkit.entity.EntityType
import org.bukkit.entity.Creeper
import org.bukkit.plugin.Plugin
import org.bukkit.scheduler.BukkitRunnable
import org.bukkit.Location

class CreeperDeathListener(
  val plugin: Plugin,
  private val allayCount: Int,
) : AbstractFeature(), Listener {

  companion object : FeatureContainerFactory<FeatureContainer> {

    override fun create(state: BuilderState): FeatureContainer =
      ListenerContainer(
        CreeperDeathListener(
          plugin = state.plugin,
          allayCount = state.config.getInt("creeperdeath.allay_count"),
        )
      )

  }

  private class SummonEntityRunnable(val target: EntityType, val location: Location, val count: Int) : BukkitRunnable() {
    override fun run() {
      for (i in 1..count) {
        location.world!!.spawnEntity(location, target)
      }
    }
  }

  override val name = "creeperdeath"

  override val description = "Killing a non-charged creeper summons allays"

  @EventHandler
  fun onEntityDeath(event: EntityDeathEvent) {
    if (!isEnabled()) {
      return
    }
    val entity = event.entity
    if (entity is Creeper) {
      if (!entity.isPowered()) {
        val location = entity.location
        SummonEntityRunnable(EntityType.ALLAY, location, allayCount).runTaskLater(plugin, 2L)
      }
    }
  }

  @EventHandler
  fun onEntityExplode(event: EntityExplodeEvent) {
    if (!isEnabled()) {
      return
    }
    val entity = event.entity
    if (entity is Creeper) {
      if (!entity.isPowered()) {
        val location = entity.location
        SummonEntityRunnable(EntityType.ALLAY, location, 1).runTaskLater(plugin, 2L)
      }
    }
  }

}
