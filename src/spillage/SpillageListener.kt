
package com.mercerenies.turtletroll.spillage

import com.mercerenies.turtletroll.feature.AbstractFeature

import org.bukkit.entity.Item
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDropItemEvent
import org.bukkit.event.player.PlayerDropItemEvent
import org.bukkit.event.block.BlockDropItemEvent

class SpillageListener(
  val handlers: List<SpillageHandler> = Spillage.defaultHandlers,
): AbstractFeature(), Listener {

  ///// Add a delay to this

  override val name = "spillage"

  override val description = "Dropping a filled bucket causes its contents to spill out"

  private fun considerSpilling(entity: Item) {
    for (handler in handlers) {
      if (handler.matches(entity)) {
        handler.run(entity)
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
