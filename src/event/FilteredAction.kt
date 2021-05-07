
package com.mercerenies.turtletroll.event

import org.bukkit.event.block.BlockBreakEvent

class FilteredAction<out T : BlockBreakAction>(
  val value: T,
  val filter: (BlockBreakEvent) -> Boolean,
) : BlockBreakAction {

  override fun shouldTrigger(event: BlockBreakEvent): Boolean {
    return filter(event) && value.shouldTrigger(event)
  }

  override fun trigger(event: BlockBreakEvent) {
    value.trigger(event)
  }

}

// This is a really interesting case. I could define filter on
// BlockBreakAction directly, but then it would only be able to return
// FilteredAction<BlockBreakAction>. By using an extension method, we
// actually get a *better* type signature out of it.
fun<T : BlockBreakAction> T.filter(func: (BlockBreakEvent) -> Boolean): FilteredAction<T> =
  FilteredAction(this, func)
