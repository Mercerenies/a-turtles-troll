
package com.mercerenies.turtletroll.drop

import com.mercerenies.turtletroll.Weight
import com.mercerenies.turtletroll.sample

import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockBreakEvent

class BlockBreakEventListener(
  private val overrideRules: List<BlockBreakAction>,
  private val actions: List<Weight<BlockBreakAction>>,
) : Listener {

  @EventHandler
  fun onBlockBreak(event: BlockBreakEvent) {
    for (action in overrideRules) {
      if (action.shouldTrigger(event)) {
        action.trigger(event)
        if (action.fullyOverridesOthers()) {
          // If any single "full override" happens, then stop the
          // function. Otherwise, keep running as normal.
          return
        }
      }
    }
    // Regular dice roll
    val validActions = actions.filter { it.value.shouldTrigger(event) }
    if (!validActions.isEmpty()) {
      val action = sample(validActions)
      action.trigger(event)
    }
  }

}
