
package com.mercerenies.turtletroll.spillage

import com.mercerenies.turtletroll.feature.AbstractFeature
import com.mercerenies.turtletroll.Constants
import com.mercerenies.turtletroll.feature.container.FeatureContainer
import com.mercerenies.turtletroll.feature.container.ListenerContainer
import com.mercerenies.turtletroll.feature.builder.BuilderState
import com.mercerenies.turtletroll.feature.builder.FeatureContainerFactory

import org.bukkit.entity.Item
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDropItemEvent
import org.bukkit.event.player.PlayerDropItemEvent
import org.bukkit.event.block.BlockDropItemEvent
import org.bukkit.scheduler.BukkitRunnable
import org.bukkit.plugin.Plugin

class SpillageListener(
  val plugin: Plugin,
  val handlers: List<SpillageHandler> = Spillage.defaultHandlers,
) : AbstractFeature(), Listener {

  companion object : FeatureContainerFactory<FeatureContainer> {
    val DELAY: Int = Constants.TICKS_PER_SECOND / 2
    override fun create(state: BuilderState): FeatureContainer =
      ListenerContainer(SpillageListener(state.plugin))

  }

  override val name = "spillage"

  override val description = "Dropping a filled bucket causes its contents to spill out"

  private class SpillageRunnable(
    val handler: SpillageHandler,
    val entity: Item,
  ) : BukkitRunnable() {
    override fun run() {
      // Only run if the entity still exists in the world.
      if (entity.isValid()) {
        handler.run(entity)
      }
    }
  }

  private fun considerSpilling(entity: Item) {
    for (handler in handlers) {
      if (handler.matches(entity)) {
        SpillageRunnable(handler, entity).runTaskLater(plugin, DELAY.toLong())
        break
      }
    }
  }

  @EventHandler
  fun onPlayerDropItem(event: PlayerDropItemEvent) {
    if (!isEnabled()) {
      return
    }
    considerSpilling(event.itemDrop)
  }

  @EventHandler
  fun onEntityDropItem(event: EntityDropItemEvent) {
    if (!isEnabled()) {
      return
    }
    considerSpilling(event.itemDrop)
  }

  @EventHandler
  fun onBlockDropItem(event: BlockDropItemEvent) {
    if (!isEnabled()) {
      return
    }
    for (item in event.items) {
      considerSpilling(item)
    }
  }

}
