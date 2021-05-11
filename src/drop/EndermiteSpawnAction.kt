
package com.mercerenies.turtletroll.drop

import org.bukkit.Material
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.entity.EntityType

object EndermiteSpawnAction : BlockBreakAction {

  override fun shouldTrigger(event: BlockBreakEvent): Boolean =
    event.block.type == Material.END_STONE

  override fun trigger(event: BlockBreakEvent) {
    val w = event.block.world
    val loc = event.block.location.add(0.5, 0.5, 0.5)

    event.block.type = Material.AIR
    event.setCancelled(true)

    w.spawnEntity(loc, EntityType.ENDERMITE)

  }

}
