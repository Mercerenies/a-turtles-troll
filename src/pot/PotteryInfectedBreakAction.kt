
package com.mercerenies.turtletroll.pot

import com.mercerenies.turtletroll.drop.BlockBreakAction
import com.mercerenies.turtletroll.drop.Positivity
import com.mercerenies.turtletroll.feature.HasEnabledStatus

import org.bukkit.Material
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.entity.EntityType

class PotteryInfectedBreakAction(
    private val parentFeature: HasEnabledStatus,
) : BlockBreakAction {
  val ENTITY_TYPES = listOf(EntityType.ENDERMITE, EntityType.SILVERFISH)

  override val positivity = Positivity.NEGATIVE

  override fun shouldTrigger(event: BlockBreakEvent): Boolean =
    parentFeature.isEnabled() && event.block.type == Material.DECORATED_POT

  override fun trigger(event: BlockBreakEvent) {
    val w = event.block.world
    val loc = event.block.location.add(0.5, 0.5, 0.5)

    event.block.type = Material.AIR
    event.setCancelled(true)

    for (_i in 1..10) {
      w.spawnEntity(loc, ENTITY_TYPES.random())
    }
  }

}
