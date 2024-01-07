
package com.mercerenies.turtletroll.falling

import org.bukkit.entity.Player
import org.bukkit.Material
import org.bukkit.block.Block

abstract class BlockDropper() {

  open val minDropHeight: Int = 1

  abstract val maxDropHeight: Int

  abstract fun getBlockToDrop(): Material

  open fun canDropThroughBlock(block: Block): Boolean =
    block.isEmpty()

  fun doDrop(player: Player) {
    val loc = player.location
    loc.y += minDropHeight
    var maxDistLeft = (maxDropHeight - minDropHeight)
    while ((maxDistLeft > 0) && (canDropThroughBlock(loc.getBlock()))) {
      maxDistLeft -= 1
      loc.y += 1
    }
    loc.y -= 1
    if (loc.getBlock().type == Material.AIR) {
      loc.getBlock().type = getBlockToDrop()
    }
  }

}
