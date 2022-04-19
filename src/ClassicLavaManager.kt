
package com.mercerenies.turtletroll

import com.mercerenies.turtletroll.ext.*
import com.mercerenies.turtletroll.dripstone.EqBlock
import com.mercerenies.turtletroll.feature.RunnableFeature

import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.EventPriority
import org.bukkit.event.block.BlockPlaceEvent
import org.bukkit.event.block.BlockFromToEvent
import org.bukkit.Material
import org.bukkit.World
import org.bukkit.plugin.Plugin
import org.bukkit.block.Block
import org.bukkit.block.`data`.Levelled
import org.bukkit.metadata.FixedMetadataValue

import kotlin.random.Random
import kotlin.math.min

class ClassicLavaManager(plugin: Plugin) : RunnableFeature(plugin), Listener {

  companion object {

    val IGNORE_DELAY_TIME = Constants.TICKS_PER_SECOND * 60L

    private val metadataTag = "com.mercerenies.turtletroll.ClassicLavaManager.metadataTag"

    fun getDefaultSpreadForWorld(env: World.Environment): Int =
      when (env) {
        World.Environment.CUSTOM -> 0 // *shrug* idk what this world is
        World.Environment.NETHER -> 8
        World.Environment.NORMAL -> 4
        World.Environment.THE_END -> 0 // The End is chaotic enough already
      }

  }

  override val name: String = "classiclava"

  override val description: String = "Lava spreads much further than usual"

  override val taskPeriod: Long = Constants.TICKS_PER_SECOND.toLong()

  val ignorer: BlockIgnorer = object : BlockIgnorer {
    override fun ignore(block: Block) {
      setBlockSpread(block, 0)
    }
  }

  private fun removeBlockSpread(block: Block) {
    block.removeMetadata(metadataTag, plugin)
  }

  private fun setBlockSpread(block: Block, spread: Int) {
    block.removeMetadata(metadataTag, plugin)
    block.setMetadata(metadataTag, FixedMetadataValue(plugin, spread))
  }

  private fun getBlockSpread(block: Block): Int {
    val meta = block.getMetadata(metadataTag)
    if (meta.size > 0) {
      return meta[0].asInt()
    } else {
      val env = block.world.environment
      return getDefaultSpreadForWorld(env)
    }
  }

  @EventHandler(priority=EventPriority.LOW)
  fun onBlockPlace(event: BlockPlaceEvent) {
    if (!isEnabled()) {
      return
    }
    if (event.block.type != Material.LAVA) {
      removeBlockSpread(event.block)
    }
  }

  @EventHandler
  fun onBlockFromTo(event: BlockFromToEvent) {
    if (!isEnabled()) {
      return
    }
    val from = event.getBlock()
    val to = event.getToBlock()
    if (from.type == Material.LAVA) {
      val spread = getBlockSpread(from)
      setBlockSpread(to, spread - 1)
      if (spread <= 0) {
        // Done spreading; ignore
        return
      }
      to.type = Material.LAVA
      val blockData = to.getBlockData()
      if (blockData is Levelled) {
        blockData.setLevel(0) // Make it a source block
        to.setBlockData(blockData)
        event.setCancelled(true)
      }
    }
  }

  override fun run() {
    if (!isEnabled()) {
      return
    }
    // Unused; exists for legacy reasons
  }

}
