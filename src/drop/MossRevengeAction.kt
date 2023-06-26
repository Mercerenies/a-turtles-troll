
package com.mercerenies.turtletroll.drop

import org.bukkit.Material
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.entity.Slime

class MossRevengeAction(
  private val slimeSize: Int,
) : BlockBreakAction {

  override fun shouldTrigger(event: BlockBreakEvent): Boolean =
    event.block.type == Material.MOSS_BLOCK

  override fun trigger(event: BlockBreakEvent) {
    val w = event.block.world
    val loc = event.block.location.add(0.5, 0.5, 0.5)

    event.block.type = Material.AIR
    event.setCancelled(true)

    val slime = w.spawn(loc, Slime::class.java)
    slime.setSize(slimeSize)

  }

}
