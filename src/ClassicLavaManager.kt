
package com.mercerenies.turtletroll

import com.mercerenies.turtletroll.util.component.*
import com.mercerenies.turtletroll.config.CheckedConfigOptions
import com.mercerenies.turtletroll.feature.RunnableFeature
import com.mercerenies.turtletroll.feature.container.FeatureContainer
import com.mercerenies.turtletroll.feature.container.ManagerContainer
import com.mercerenies.turtletroll.feature.builder.BuilderState
import com.mercerenies.turtletroll.feature.builder.FeatureContainerFactory

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

class ClassicLavaManager(
  plugin: Plugin,
  private val worldSpreadMap: Map<World.Environment, Int>,
) : RunnableFeature(plugin), Listener {

  companion object : FeatureContainerFactory<FeatureContainer> {

    val IGNORE_DELAY_TIME = Constants.TICKS_PER_SECOND * 60L

    val STORAGE_IGNORER_KEY = "com.mercerenies.turtletroll.ClassicLavaManager.STORAGE_IGNORER_KEY"

    private val metadataTag = "com.mercerenies.turtletroll.ClassicLavaManager.metadataTag"

    override fun create(state: BuilderState): FeatureContainer {
      val manager = ClassicLavaManager(state.plugin, loadWorldSpreadMap(state.config))
      state.registerSharedData(STORAGE_IGNORER_KEY, manager.ignorer)
      return ManagerContainer(manager)
    }

    private fun loadWorldSpreadMap(config: CheckedConfigOptions): Map<World.Environment, Int> =
      mapOf(
        World.Environment.NORMAL to config.getInt("classiclava.normal_spread"),
        World.Environment.NETHER to config.getInt("classiclava.nether_spread"),
        World.Environment.THE_END to config.getInt("classiclava.end_spread"),
      )

  }

  override val name: String = "classiclava"

  override val description: String = "Lava spreads much further than usual"

  override val taskPeriod: Long = Constants.TICKS_PER_SECOND.toLong()

  val ignorer: BlockIgnorer = object : BlockIgnorer {
    override fun ignore(block: Block) {
      setBlockSpread(block, 0)
    }
  }

  private fun getDefaultSpreadForWorld(env: World.Environment): Int =
    worldSpreadMap[env] ?: 0

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

  @EventHandler(priority = EventPriority.LOW)
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
      if (to.type == Material.WATER) {
        // Do not do classic lava when spreading onto water, as that
        // interrupts Minecraft's (and SolidSwapListener's) block form
        // events.
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
