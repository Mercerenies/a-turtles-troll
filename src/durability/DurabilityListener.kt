
package com.mercerenies.turtletroll.durability

import com.mercerenies.turtletroll.feature.AbstractFeature

import org.bukkit.Material
import org.bukkit.Location
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.block.Block
import org.bukkit.Sound

import kotlin.collections.HashMap

abstract class DurabilityListener() : AbstractFeature(), Listener {

  companion object {

    private fun destroyBlock(block: Block) {
      block.type = Material.AIR
    }

  }

  private val memory = HashMap<Location, Int>()

  abstract val maxUses: Int

  abstract fun shouldAffect(block: Block): Boolean

  // For most ordinary blocks, this just gets the location of the
  // block. But if the block is part of a multi-block structure, such
  // as a 2-block-high door, then this should normalize in some way so
  // that both blocks return the same location. Subclasses are
  // responsible for ensuring this behavior when necessary.
  open fun adjustBlockPosition(block: Block): Location =
    block.location

  @EventHandler
  fun onPlayerInteract(event: PlayerInteractEvent) {
    if (!isEnabled()) {
      return
    }
    val block = event.getClickedBlock()
    if ((block != null) && (shouldAffect(block))) {
      val loc = adjustBlockPosition(block)
      decrementValue(event, loc)
    }
  }

  @EventHandler
  fun onBlockBreak(event: BlockBreakEvent) {
    // Note: This is NOT a BlockBreakAction; it triggers on its own
    // (this class is smart enough to understand how to properly
    // destroy doors, unlike CancelDropAction.
    if (!isEnabled()) {
      return
    }
    if (shouldAffect(event.block)) {
      event.setCancelled(true)
      var loc = adjustBlockPosition(event.block)
      destroyBlock(loc.block)
    }
  }

  private fun decrementValue(event: PlayerInteractEvent, loc: Location) {
    val uses = memory[loc] ?: maxUses
    loc.world!!.playSound(loc, Sound.ENTITY_ZOMBIE_ATTACK_WOODEN_DOOR, 1.0f, 0.0f)
    if (uses == 1) {
      memory.remove(loc)
      event.setCancelled(true)
      destroyBlock(loc.block)
    } else {
      memory[loc] = uses - 1
    }
  }

}
