
package com.mercerenies.turtletroll.event

import com.mercerenies.turtletroll.ext.*

import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.entity.EntityType

import kotlin.random.Random

class SilverfishAttackAction(
  val bombData: SilverfishAttackBomb = SilverfishAttackBomb.DEFAULT
) : BlockBreakAction {

  override fun shouldTrigger(event: BlockBreakEvent): Boolean =
    Infestation.canInfest(event.block.type)

  override fun trigger(event: BlockBreakEvent) {
    val w = event.block.world
    val brokenLoc = event.block.location.add(0.5, 0.5, 0.5)

    event.block.type = Material.AIR
    event.setCancelled(true)

    w.spawnEntity(brokenLoc, EntityType.SILVERFISH)

    if (Random.nextDouble() < bombData.probability) {
      for (loc in brokenLoc.nearby(bombData.radius)) {
        val block = w.getBlockAt(loc)
        if (!Infestation.canInfest(block.type)) {
          continue
        }
        bombData.rollForBlock().run(block)
      }
    }

  }

}
