
package com.mercerenies.turtletroll.drop.nearby

import com.mercerenies.turtletroll.ext.*

import org.bukkit.Material
import org.bukkit.Location
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.entity.EntityType
import org.bukkit.entity.Bee


class BeeAttackAction(
  private val radius: Int = 5
) : NearbyAction {

  companion object {
    val BLOCKS = setOf(
      Material.OAK_LOG, Material.OAK_PLANKS, Material.STRIPPED_OAK_LOG, Material.STRIPPED_OAK_WOOD,
      Material.SPRUCE_LOG, Material.SPRUCE_PLANKS, Material.STRIPPED_SPRUCE_LOG, Material.STRIPPED_SPRUCE_WOOD,
      Material.JUNGLE_LOG, Material.JUNGLE_PLANKS, Material.STRIPPED_JUNGLE_LOG, Material.STRIPPED_JUNGLE_WOOD,
      Material.BIRCH_LOG, Material.BIRCH_PLANKS, Material.STRIPPED_BIRCH_LOG, Material.STRIPPED_BIRCH_WOOD,
      Material.ACACIA_LOG, Material.ACACIA_PLANKS, Material.STRIPPED_ACACIA_LOG, Material.STRIPPED_ACACIA_WOOD,
      Material.DARK_OAK_LOG, Material.DARK_OAK_PLANKS, Material.STRIPPED_DARK_OAK_LOG, Material.STRIPPED_DARK_OAK_WOOD,
      Material.CRIMSON_HYPHAE, Material.CRIMSON_PLANKS, Material.CRIMSON_STEM, Material.STRIPPED_CRIMSON_HYPHAE, Material.STRIPPED_CRIMSON_STEM,
      Material.WARPED_HYPHAE, Material.WARPED_PLANKS, Material.WARPED_STEM, Material.WARPED_HYPHAE, Material.WARPED_STEM,
    )
  }

  override fun shouldTrigger(event: BlockBreakEvent): Boolean =
    BLOCKS.contains(event.block.type)

  private fun spawnBees(event: BlockBreakEvent, loc: Location) {
    val bee = event.block.world.spawnEntity(loc, EntityType.BEE) as Bee
    bee.anger = 100
    bee.target = event.player
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
