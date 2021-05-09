
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
    val overrideAction = overrideRules.find { it.shouldTrigger(event) }
    if (overrideAction != null) {
      // Override
      overrideAction.trigger(event);
    } else {
      // Regular dice roll
      val validActions = actions.filter { it.value.shouldTrigger(event) }
      if (!validActions.isEmpty()) {
        val action = sample(validActions)
        action.trigger(event)
      }
    }
  }

}
