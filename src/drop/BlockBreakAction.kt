
package com.mercerenies.turtletroll.drop

import org.bukkit.event.block.BlockBreakEvent

interface BlockBreakAction {

  fun shouldTrigger(event: BlockBreakEvent): Boolean

  fun fullyOverridesOthers(): Boolean = true

  fun trigger(event: BlockBreakEvent)

}
