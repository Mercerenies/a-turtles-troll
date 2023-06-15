
package com.mercerenies.turtletroll

import com.mercerenies.turtletroll.feature.AbstractFeature
import com.mercerenies.turtletroll.feature.container.FeatureContainer
import com.mercerenies.turtletroll.feature.container.ListenerContainer
import com.mercerenies.turtletroll.feature.builder.BuilderState
import com.mercerenies.turtletroll.feature.builder.FeatureContainerFactory

import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityBreedEvent
import org.bukkit.entity.EntityType
import org.bukkit.plugin.Plugin
import org.bukkit.scheduler.BukkitRunnable
import org.bukkit.Location

import net.kyori.adventure.text.Component

import kotlin.random.Random

class JohnnyListener(
  val plugin: Plugin,
  val chance: Double = 0.1,
) : AbstractFeature(), Listener {

  companion object : FeatureContainerFactory<FeatureContainer> {

    override fun create(state: BuilderState): FeatureContainer =
      ListenerContainer(JohnnyListener(state.plugin))

  }

  private class SummonJohnnyRunnable(val location: Location) : BukkitRunnable() {
    override fun run() {
      val entity = location.world!!.spawnEntity(location, EntityType.VINDICATOR)
      entity.customName(Component.text("Johnny"))
      Messages.broadcastMessage("Heeeeeeeeere's Johnny!")
    }
  }

  override val name = "johnny"

  override val description = "Breeding animals sometimes produces a Vindicator named Johnny"

  @EventHandler
  fun onEntityBreed(event: EntityBreedEvent) {
    if (!isEnabled()) {
      return
    }
    if (event.isCancelled()) {
      return
    }
    if (Random.nextDouble() < chance) {
      event.setCancelled(true)
      SummonJohnnyRunnable(event.entity.location).runTaskLater(plugin, 2L)
    }
  }

}
