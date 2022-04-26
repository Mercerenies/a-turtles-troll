
package com.mercerenies.turtletroll.drop

import com.mercerenies.turtletroll.Weight
import com.mercerenies.turtletroll.sample
import com.mercerenies.turtletroll.feature.container.DropFeatureContainer

import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockBreakEvent

class BlockBreakEventListener(
  private val preRules: List<BlockBreakAction>,
  private val actions: List<Weight<BlockBreakAction>>,
  private val postRules: List<BlockBreakAction>,
) : Listener {

  constructor(dropFeature: DropFeatureContainer)
    : this(
        dropFeature.preRules.toList(),
        dropFeature.actions.toList(),
        dropFeature.postRules.toList(),
      ) {}

  @EventHandler
  fun onBlockBreak(event: BlockBreakEvent) {

    for (action in preRules) {
      if (action.shouldTrigger(event)) {
        action.trigger(event)
        if (action.fullyOverridesOthers()) {
          return
        }
      }
    }

    // Regular dice roll
    val validActions = actions.filter { it.value.shouldTrigger(event) }
    if (!validActions.isEmpty()) {
      val action = sample(validActions)
      action.trigger(event)
      if (action.fullyOverridesOthers()) {
        return
      }
    }

    for (action in postRules) {
      if (action.shouldTrigger(event)) {
        action.trigger(event)
        if (action.fullyOverridesOthers()) {
          return
        }
      }
    }

  }

}
