
package com.mercerenies.turtletroll.drop

import org.bukkit.Material
import org.bukkit.event.block.BlockBreakEvent

class CancelAndReplaceAction(val material: Material) : BlockBreakAction {

  override fun shouldTrigger(event: BlockBreakEvent): Boolean = true

  override fun trigger(event: BlockBreakEvent) {
    event.block.type = material
    event.setCancelled(true)
  }

}
