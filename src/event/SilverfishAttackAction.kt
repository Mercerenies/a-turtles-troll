
package com.mercerenies.turtletroll.event

import org.bukkit.Material
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.entity.Silverfish
import org.bukkit.entity.EntityType

object SilverfishAttackAction : BlockBreakAction {

  override fun shouldTrigger(event: BlockBreakEvent): Boolean =
    Infestation.canInfest(event.block.type)

  override fun trigger(event: BlockBreakEvent) {
    val w = event.block.world
    val loc = event.block.location.add(0.5, 0.5, 0.5)

    event.block.type = Material.AIR
    event.setCancelled(true)

    val silverfish = w.spawnEntity(loc, EntityType.SILVERFISH) as Silverfish

  }

}
