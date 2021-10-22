
package com.mercerenies.turtletroll.drop

import com.mercerenies.turtletroll.ext.*

import org.bukkit.Material
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.entity.Item
import org.bukkit.entity.EntityType

class ShuffleDropsAction(
  val types: List<Material>,
) : BlockBreakAction {

  override fun shouldTrigger(event: BlockBreakEvent): Boolean {
    val defaultDrops = event.getDefaultDrops()
    return !defaultDrops.isEmpty() && defaultDrops.all { types.contains(it.type) }
  }

  override fun trigger(event: BlockBreakEvent) {
    val w = event.block.world
    val loc = event.block.location.add(0.5, 0.5, 0.5)

    val defaultDrops = event.getDefaultDrops()
    val dropCount = defaultDrops.map { it.amount }.sum()

    event.block.type = Material.AIR
    event.setCancelled(true)

    repeat (dropCount) {
      val itemType = types.sample()!!
      val item = w.spawnEntity(loc, EntityType.DROPPED_ITEM) as Item
      item.itemStack = ItemStack(itemType, 1)
    }

  }

}
