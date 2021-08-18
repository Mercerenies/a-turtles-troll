
package com.mercerenies.turtletroll

import com.mercerenies.turtletroll.feature.Feature
import com.mercerenies.turtletroll.feature.RunnableFeature
import com.mercerenies.turtletroll.ext.*

import org.bukkit.entity.Player
import org.bukkit.plugin.Plugin
import org.bukkit.Location
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.Chunk
import org.bukkit.World
import org.bukkit.entity.Entity
import org.bukkit.block.Block
import org.bukkit.event.Listener
import org.bukkit.event.EventHandler
import org.bukkit.event.world.ChunkPopulateEvent
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType

import kotlin.random.Random

class ContagiousMossManager : RunnableFeature(), Listener {

  companion object {
    val TICKS_PER_SECOND = 20

    fun isAdjacentToMoss(block: Block): Boolean {
      for (loc in block.location.nearby(1)) {
        if (loc.block.type == Material.MOSS_BLOCK) {
          return true
        }
      }
      return false
    }

  }

  override val name = "moss"

  override val description = "Moss generates in new chunks and spreads everywhere"

  override fun run() {
    if (!isEnabled()) {
      return
    }
    val onlinePlayers = Bukkit.getOnlinePlayers().toList()
    repeat(100) {
      val player = onlinePlayers.sample()
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
      val block = BlockSelector.getRandomBlock(event.chunk, BlockSelector.SEA_LEVEL)
      block.type = Material.MOSS_BLOCK
    }
  }

  fun register(plugin: Plugin) {
    this.runTaskTimer(plugin, 1L, 4L)
  }

}
