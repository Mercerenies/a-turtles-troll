
package com.mercerenies.turtletroll

import com.mercerenies.turtletroll.feature.AbstractFeature

import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerMoveEvent
import org.bukkit.event.block.BlockPlaceEvent
import org.bukkit.block.Block
import org.bukkit.Location
import org.bukkit.plugin.Plugin
import org.bukkit.scheduler.BukkitRunnable

abstract class BreakOnSightListener(val plugin: Plugin) : AbstractFeature(), Listener {
  val memory = CooldownMemory<Location>(plugin)

  companion object {
    val TICKS_PER_SECOND = 20
  }

  // How many ticks is a block safe after being placed?
  open val safetyDelay: Int = TICKS_PER_SECOND * 3

  abstract fun shouldDrop(block: Block): Boolean

  @EventHandler
  fun onPlayerMove(event: PlayerMoveEvent) {
    if (!isEnabled()) {
      return
    }
    val player = event.getPlayer()
    val targetBlock = player.getTargetBlock(null, 32)
    if ((shouldDrop(targetBlock)) && (!memory.contains(targetBlock.location))) {
      targetBlock.breakNaturally()
    }
  }

  @EventHandler
  fun onBlockPlace(event: BlockPlaceEvent) {
    if (!isEnabled()) {
      return
    }
    val targetBlock = event.getBlockPlaced()
    if (shouldDrop(targetBlock)) {
      memory.add(targetBlock.location, safetyDelay.toLong())
    }
  }

}
