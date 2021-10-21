
package com.mercerenies.turtletroll.drop.nearby

import com.mercerenies.turtletroll.ext.*

import org.bukkit.Material
import org.bukkit.Location
import org.bukkit.event.block.BlockBreakEvent


class NetherrackBoomAction(
  private val radius: Int = 5
) : NearbyAction {

  companion object {
    val BLOCK_TYPES = setOf(Material.NETHERRACK, Material.CRIMSON_NYLIUM, Material.WARPED_NYLIUM,
                            Material.SOUL_SAND, Material.SOUL_SOIL, Material.NETHER_BRICKS,
                            Material.CRACKED_NETHER_BRICKS, Material.CHISELED_NETHER_BRICKS)
  }

  override fun shouldTrigger(event: BlockBreakEvent): Boolean =
    BLOCK_TYPES.contains(event.block.type)

  override fun fullyOverridesOthers(): Boolean = false

  override fun onActivate(event: BlockBreakEvent) {}

  override fun onActivateNearby(event: BlockBreakEvent, loc: Location) {
    if (BLOCK_TYPES.contains(loc.block.type)) {
      loc.block.type = Material.AIR
    }
  }

  override fun getRadius(event: BlockBreakEvent): Int = radius

}
