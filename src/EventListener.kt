
package com.mercerenies.turtletroll

import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.World
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.entity.Item
import org.bukkit.entity.EntityType

class EventListener : Listener {

  @EventHandler
  fun onBlockBreak(event: BlockBreakEvent) {
    if (Math.random() < 0.5) {
      val defaultDrops = event.block.getDrops(event.player.inventory.itemInMainHand, event.player)
      if (!defaultDrops.isEmpty()) {
        val w = event.block.world
        event.setDropItems(false)
        val item = w.spawnEntity(event.block.location, EntityType.DROPPED_ITEM) as Item
        item.itemStack = ItemStack(Material.DIRT, 64)
      }
    }
  }

}
