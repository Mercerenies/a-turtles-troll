
package com.mercerenies.turtletroll

import com.mercerenies.turtletroll.feature.AbstractFeature

import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerMoveEvent
import org.bukkit.block.Block

abstract class BreakOnSightListener() : AbstractFeature(), Listener {

  abstract fun shouldDrop(block: Block): Boolean

  @EventHandler
  fun onPlayerMove(event: PlayerMoveEvent) {
    if (!isEnabled()) {
      return
    }
    val player = event.getPlayer()
    val targetBlock = player.getTargetBlock(null, 32)
    if (shouldDrop(targetBlock)) {
      targetBlock.breakNaturally()
    }
  }

}
