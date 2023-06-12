
package com.mercerenies.turtletroll

import com.mercerenies.turtletroll.feature.AbstractFeature
import com.mercerenies.turtletroll.feature.container.FeatureContainer
import com.mercerenies.turtletroll.feature.container.ListenerContainer
import com.mercerenies.turtletroll.feature.builder.BuilderState
import com.mercerenies.turtletroll.feature.builder.FeatureContainerFactory

import org.bukkit.Material
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerMoveEvent
import org.bukkit.Location
import org.bukkit.scheduler.BukkitRunnable
import org.bukkit.plugin.Plugin

import kotlin.collections.HashSet

class ForestFireListener(val plugin: Plugin) : AbstractFeature(), Listener {
  private var memory = HashSet<Location>()

  override val name = "forestfire"

  override val description = "Leaves and ice catch fire when you walk on them"

  companion object : FeatureContainerFactory<FeatureContainer> {
    val DELAY = Constants.TICKS_PER_SECOND
    val BLOCKS = BlockTypes.LEAVES union setOf(Material.BLUE_ICE, Material.FROSTED_ICE, Material.ICE, Material.PACKED_ICE)

    override fun create(state: BuilderState): FeatureContainer =
      ListenerContainer(ForestFireListener(state.plugin))

  }

  private inner class DelayedFire(val location: Location) : BukkitRunnable() {
    override fun run() {
      memory.remove(location)
      location.block.type = Material.FIRE
    }
  }

  @EventHandler
  fun onPlayerMove(event: PlayerMoveEvent) {
    if (!isEnabled()) {
      return
    }
    val loc = event.getTo()
    val block = loc.clone().add(0.0, -1.0, 0.0).getBlock()
    if (BLOCKS.contains(block.type)) {
      if (!memory.contains(loc)) {
        memory.add(loc)
        DelayedFire(loc).runTaskLater(plugin, DELAY.toLong())
      }
    }
  }

}
