
package com.mercerenies.turtletroll.drop

import com.mercerenies.turtletroll.util.EventUtils

import org.bukkit.Material
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.entity.Item
import org.bukkit.entity.EntityType

class ReplaceDropsAction(
  val itemStack: ItemStack,
  override val positivity: Positivity,
) : BlockBreakAction {

  override fun shouldTrigger(event: BlockBreakEvent): Boolean {
    return !EventUtils.getDefaultDrops(event).isEmpty()
  }

  override fun trigger(event: BlockBreakEvent) {
    val w = event.block.world
    val loc = event.block.location.add(0.5, 0.5, 0.5)

    event.block.type = Material.AIR
    event.setCancelled(true)

    val item = w.spawnEntity(loc, EntityType.ITEM) as Item
    item.itemStack = itemStack.clone()

  }

}
