
package com.mercerenies.turtletroll

import com.mercerenies.turtletroll.feature.RunnableFeature
import com.mercerenies.turtletroll.feature.container.FeatureContainer
import com.mercerenies.turtletroll.feature.container.ManagerContainer
import com.mercerenies.turtletroll.feature.builder.BuilderState
import com.mercerenies.turtletroll.feature.builder.FeatureContainerFactory
import com.mercerenies.turtletroll.location.BlockSelector

import org.bukkit.plugin.Plugin
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.World
import org.bukkit.block.Block
import org.bukkit.event.Listener
import org.bukkit.event.EventHandler
import org.bukkit.event.world.ChunkPopulateEvent

class ContagiousMossManager(plugin: Plugin) : RunnableFeature(plugin), Listener {

  companion object : FeatureContainerFactory<FeatureContainer> {

    override fun create(state: BuilderState): FeatureContainer =
      ManagerContainer(ContagiousMossManager(state.plugin))

    fun isAdjacentToMoss(block: Block): Boolean {
      for (loc in BlockSelector.getNearby(block.location, 1)) {
        if (loc.block.type == Material.MOSS_BLOCK) {
          return true
        }
      }
      return false
    }

  }

  override val name = "moss"

  override val description = "Moss generates in new chunks and spreads everywhere"

  override val taskPeriod = 4L

  override fun run() {
    if (!isEnabled()) {
      return
    }
    val onlinePlayers = Bukkit.getOnlinePlayers().toList()
    repeat(100) {
      val player = onlinePlayers.randomOrNull()
      if (player != null) {
        if (player.world.environment == World.Environment.NORMAL) {
          val playerPosBlock = player.location.block
          val randBlock = BlockSelector.getRandomBlockNear(playerPosBlock)
          if (isAdjacentToMoss(randBlock)) {
            randBlock.type = Material.MOSS_BLOCK
            return // Only modify one block per test
          }
        }
      }
    }
  }

  @EventHandler
  fun onChunkPopulate(event: ChunkPopulateEvent) {
    if (!isEnabled()) {
      return
    }
    if (event.chunk.world.environment == World.Environment.NORMAL) {
      // Unconditionally get a block and convert it to moss block
      val block = BlockSelector.getRandomBlock(event.chunk, BlockSelector.BEDROCK, BlockSelector.SEA_LEVEL)
      block.type = Material.MOSS_BLOCK
    }
  }

}
