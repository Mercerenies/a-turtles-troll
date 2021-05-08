
package com.mercerenies.turtletroll.drop

import org.bukkit.event.block.BlockBreakEvent

object NullAction : BlockBreakAction {

  override fun shouldTrigger(event: BlockBreakEvent): Boolean = true

  override fun trigger(event: BlockBreakEvent) {}

}
