
package com.mercerenies.turtletroll.drop

import org.bukkit.Material
import org.bukkit.event.block.BlockBreakEvent

object CancelDropAction : BlockBreakAction {

  override fun shouldTrigger(event: BlockBreakEvent): Boolean = true

  override fun trigger(event: BlockBreakEvent) {
    event.block.type = Material.AIR
    event.setCancelled(true)
  }

}
