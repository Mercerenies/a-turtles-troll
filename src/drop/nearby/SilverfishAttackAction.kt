
package com.mercerenies.turtletroll.drop.nearby

import com.mercerenies.turtletroll.util.component.*
import com.mercerenies.turtletroll.Infestation
import com.mercerenies.turtletroll.drop.Positivity

import org.bukkit.Material
import org.bukkit.Location
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.entity.EntityType

import kotlin.random.Random

open class SilverfishAttackAction(
  val bombData: SilverfishAttackBomb = SilverfishAttackBomb.DEFAULT
) : NearbyAction {

  override val positivity = Positivity.NEGATIVE

  open override fun shouldTrigger(event: BlockBreakEvent): Boolean =
    Infestation.canInfest(event.block.type)

  override fun onActivate(event: BlockBreakEvent) {
    val loc = event.block.location.add(0.5, 0.5, 0.5)
    event.block.type = Material.AIR
    event.setCancelled(true)
    event.block.world.spawnEntity(loc, EntityType.SILVERFISH)
  }

  override fun onActivateNearby(event: BlockBreakEvent, loc: Location) {
    val w = event.block.world
    val block = w.getBlockAt(loc)
    if (Infestation.canInfest(block.type)) {
      bombData.rollForBlock().run(block)
    }
  }

  override fun getRadius(event: BlockBreakEvent): Int {
    return if (Random.nextDouble() < bombData.probability) {
      bombData.radius
    } else {
      0
    }
  }

}
