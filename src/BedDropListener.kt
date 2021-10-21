
package com.mercerenies.turtletroll

import com.mercerenies.turtletroll.feature.AbstractFeature

import org.bukkit.Material
import org.bukkit.Location
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.block.Block
import org.bukkit.block.`data`.`type`.Bed


// CancelDropAction is not powerful enough to block bed drops because
// beds are multiple blocks, so we handle them specially here.
class BedDropListener() : AbstractFeature(), Listener {

  companion object {

    private fun destroyBlock(block: Block) {
      block.type = Material.AIR
    }

    fun adjustBlockPosition(block: Block): Location {
      val blockData = block.blockData
      if ((blockData is Bed) && (blockData.getPart() == Bed.Part.FOOT)) {
        val normal = blockData.facing.direction
        return block.location.clone().add(normal)
      } else {
        return block.location.clone()
      }
    }

  }

  override val name = "beddrop"

  override val description = "Beds do not drop when broken"

  @EventHandler
  fun onBlockBreak(event: BlockBreakEvent) {
    if (!isEnabled()) {
      return
    }

    val block = event.block
    if (block.blockData is Bed) {
      event.setCancelled(true)
      val loc = adjustBlockPosition(block)
      destroyBlock(loc.block)
    }
  }

}
