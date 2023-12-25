
package com.mercerenies.turtletroll.overgrowth

import com.mercerenies.turtletroll.feature.AbstractFeature
import com.mercerenies.turtletroll.Constants

import org.bukkit.Material
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockGrowEvent
import org.bukkit.plugin.Plugin
import org.bukkit.Location
import org.bukkit.scheduler.BukkitRunnable
import org.bukkit.block.`data`.Ageable

class OvergrowthListener(
  val plugin: Plugin,
  val overgrowthBlock: () -> Material,
  val delay: Long = 600, // Seconds
) : AbstractFeature(), Listener {

  companion object {
    val BLOCKS = setOf(
      Material.BEETROOTS, Material.CARROTS, Material.COCOA, Material.NETHER_WART,
      Material.POTATOES, Material.SWEET_BERRY_BUSH, Material.WHEAT,
    )

    val WOOD_BLOCKS = listOf(
      Material.SPRUCE_LOG, Material.OAK_LOG, Material.JUNGLE_LOG, Material.BIRCH_LOG,
      Material.ACACIA_LOG, Material.DARK_OAK_LOG, Material.CRIMSON_STEM, Material.WARPED_STEM,
    )

    fun randomWood(): Material =
      WOOD_BLOCKS.random()

    fun alwaysObsidian(): Material =
      Material.OBSIDIAN

  }

  private inner class TurnInto(val location: Location, val blockType: Material) : BukkitRunnable() {
    override fun run() {
      val block = location.block
      if (block.type == blockType) {
        val ageable = block.getBlockData() as Ageable
        if (ageable.getAge() == ageable.getMaximumAge()) {
          block.type = overgrowthBlock()
        }
      }
    }
  }

  override val name = "overgrowth"

  override val description = "Several plants will grow into another block if not harvested in time"

  @EventHandler
  fun onBlockGrow(event: BlockGrowEvent) {
    if (!isEnabled()) {
      return
    }
    val block = event.getBlock()
    if (!BLOCKS.contains(block.type)) {
      return
    }
    val blockData = event.getNewState().getBlockData()
    if (blockData is Ageable) {
      if (blockData.getAge() == blockData.getMaximumAge()) {
        TurnInto(block.location, block.type).runTaskLater(plugin, delay * Constants.TICKS_PER_SECOND)
      }
    }
  }

}
