
package com.mercerenies.turtletroll.event

import org.bukkit.event.block.BlockBreakEvent

interface BlockBreakAction {

  fun shouldTrigger(event: BlockBreakEvent): Boolean

  fun trigger(event: BlockBreakEvent)

}
