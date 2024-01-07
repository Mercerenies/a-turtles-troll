
package com.mercerenies.turtletroll.drop.nearby

import com.mercerenies.turtletroll.util.component.*
import com.mercerenies.turtletroll.BlockTypes
import com.mercerenies.turtletroll.EntitySpawner
import com.mercerenies.turtletroll.drop.Positivity

import org.bukkit.Material
import org.bukkit.Location
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.entity.Bee

class BeeAttackAction(
  private val radius: Int,
  private val friendlyAxe: Boolean,
) : NearbyAction {

  companion object {
    val BLOCKS =
      BlockTypes.LOGS + BlockTypes.PLANKS +
        setOf(
          Material.CRIMSON_HYPHAE, Material.CRIMSON_PLANKS, Material.CRIMSON_STEM, Material.STRIPPED_CRIMSON_HYPHAE, Material.STRIPPED_CRIMSON_STEM,
          Material.WARPED_HYPHAE, Material.WARPED_PLANKS, Material.WARPED_STEM, Material.WARPED_HYPHAE, Material.WARPED_STEM,
        )
  }

  override val positivity = Positivity.NEGATIVE

  override fun shouldTrigger(event: BlockBreakEvent): Boolean {
    if (!BLOCKS.contains(event.block.type)) {
      return false
    }
    // Disable if the player is using a wooden axe
    if ((friendlyAxe) && (event.player.inventory.itemInMainHand.type == Material.WOODEN_AXE)) {
      return false
    }
    return true
  }

  private fun spawnBees(event: BlockBreakEvent, loc: Location) {
    val bee = EntitySpawner.spawnCreature(loc, Bee::class.java)
    if (bee != null) {
      bee.anger = 100
      bee.target = event.player
    }
  }

  override fun onActivate(event: BlockBreakEvent) {
    val loc = event.block.location.add(0.5, 0.5, 0.5)
    event.block.type = Material.AIR
    event.setCancelled(true)
    spawnBees(event, loc)
  }

  override fun onActivateNearby(event: BlockBreakEvent, loc: Location) {
    val w = event.block.world
    val block = w.getBlockAt(loc)
    if (BLOCKS.contains(block.type)) {
      block.type = Material.AIR
      spawnBees(event, loc)
    }
  }

  override fun getRadius(event: BlockBreakEvent): Int = radius

}
