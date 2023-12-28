
package com.mercerenies.turtletroll.drop.nearby

import com.mercerenies.turtletroll.location.BlockSelector
import com.mercerenies.turtletroll.drop.BlockBreakAction

import org.bukkit.Location
import org.bukkit.event.block.BlockBreakEvent

interface NearbyAction : BlockBreakAction {

  override fun trigger(event: BlockBreakEvent) {
    val brokenLoc = event.block.location.add(0.5, 0.5, 0.5)

    onActivate(event)

    val radius = getRadius(event)
    if (radius > 0) {
      for (loc in BlockSelector.getNearby(brokenLoc, radius)) {
        if (loc != brokenLoc) {
          onActivateNearby(event, loc)
        }
      }
    }

  }

  fun onActivate(event: BlockBreakEvent)

  fun onActivateNearby(event: BlockBreakEvent, loc: Location)

  fun getRadius(event: BlockBreakEvent): Int

}
