
package com.mercerenies.turtletroll.event

import org.bukkit.Material
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.entity.Item
import org.bukkit.entity.EntityType

class ReplaceDropsAction(val itemStack: ItemStack) : BlockBreakAction {

  override fun shouldTrigger(event: BlockBreakEvent): Boolean {
    val player = event.player
    val defaultDrops = event.block.getDrops(player.inventory.itemInMainHand, player)
    return !defaultDrops.isEmpty()
  }

  override fun trigger(event: BlockBreakEvent) {
    val w = event.block.world
    val loc = event.block.location
    val item = w.spawnEntity(loc, EntityType.DROPPED_ITEM) as Item

    event.block.type = Material.AIR
    event.setCancelled(true)
    item.itemStack = itemStack.clone()

  }

}
