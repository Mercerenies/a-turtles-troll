
package com.mercerenies.turtletroll.util

import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.inventory.ItemStack

// Helpers for a variety of built-in Bukkit event classes.
object EventUtils {

  fun getDefaultDrops(event: BlockBreakEvent): Collection<ItemStack> =
    event.block.getDrops(event.player.inventory.itemInMainHand, event.player)

}
