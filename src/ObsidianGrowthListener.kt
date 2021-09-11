
package com.mercerenies.turtletroll

import com.mercerenies.turtletroll.feature.AbstractFeature

import org.bukkit.Material
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockGrowEvent
import org.bukkit.entity.Player
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import org.bukkit.inventory.meta.Damageable
import org.bukkit.plugin.Plugin
import org.bukkit.Location
import org.bukkit.scheduler.BukkitRunnable
import org.bukkit.block.`data`.Ageable

import kotlin.random.Random

class ObsidianGrowthListener(
  val plugin: Plugin,
  val delay: Long = 600, // Seconds
): AbstractFeature(), Listener {

  companion object {
    val TICKS_PER_SECOND = 20L
    val BLOCKS = setOf(
      Material.BEETROOTS, Material.CARROTS, Material.COCOA, Material.NETHER_WART,
      Material.POTATOES, Material.SWEET_BERRY_BUSH, Material.WHEAT,
    )
  }

  private class TurnToObsidian(val location: Location, val blockType: Material): BukkitRunnable() {
    override fun run() {
      val block = location.block
      if (block.type == blockType) {
        val ageable = block.getBlockData() as Ageable
        if (ageable.getAge() == ageable.getMaximumAge()) {
          block.type = Material.OBSIDIAN
        }
      }
    }
  }

  override val name = "obsidian"

  override val description = "Several plants will grow into obsidian if not harvested in time"

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
        TurnToObsidian(block.location, block.type).runTaskLater(plugin, delay * TICKS_PER_SECOND)
      }
    }
  }

}
