
package com.mercerenies.turtletroll.dripstone

import com.mercerenies.turtletroll.feature.Feature
import com.mercerenies.turtletroll.feature.RunnableFeature
import com.mercerenies.turtletroll.CooldownMemory
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
import org.bukkit.block.BlockFace
import org.bukkit.block.`data`.type.PointedDripstone
import org.bukkit.event.Listener
import org.bukkit.event.EventHandler
import org.bukkit.event.player.PlayerMoveEvent
import org.bukkit.event.block.BlockPlaceEvent
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType

import kotlin.random.Random

class DripstoneManager(val plugin: Plugin) : RunnableFeature(), Listener {

  companion object {
    val TICKS_PER_SECOND = 20

    // Find a stalactite directly above the block position. Returns
    // null if we hit another solid block first.
    fun findStalactite(block: Block): Stalactite? {
      var loc = block.location.clone()
      repeat(10) {
        val innerBlock = loc.block
        if (innerBlock.type == Material.POINTED_DRIPSTONE) {
          val blockData = innerBlock.getBlockData() as PointedDripstone
          if (blockData.getVerticalDirection() == BlockFace.DOWN) {
            return Stalactite.fromPart(innerBlock)
          } else {
            // We hit a pointed dripstone block, but it was facing the wrong way
            return null
          }
        }
        if (innerBlock.type.isSolid()) {
          // Some other solid block, so abort search
          return null
        }
        loc.setY(loc.getY() + 1)
      }
      return null
    }

  }

  private val _placedBlocks = CooldownMemory<Stalactite>(plugin)

  override val name = "dripstone"

  override val description = "Dripstone generates randomly and falls if you walk under it"

  override fun run() {
    if (!isEnabled()) {
      return
    }
/*
    val onlinePlayers = Bukkit.getOnlinePlayers().toList()
    repeat(100) {
      val player = onlinePlayers.sample()
      if (player != null) {
        if (player.world.environment == World.Environment.NORMAL) {
          val playerPosBlock = player.location.block
          val randBlock = getRandomBlockNear(playerPosBlock)
          if (isAdjacentToMoss(randBlock)) {
            randBlock.type = Material.MOSS_BLOCK
            return // Only modify one block per test
          }
        }
      }
    }
*/
  }

  @EventHandler
  fun onPlayerMove(event: PlayerMoveEvent) {
    if (!isEnabled()) {
      return
    }
    val location = event.getTo()?.block?.location

    if (location != null) {
      for (local in location.nearbyXZ(2)) {
        // Look for a stalactite
        val stalactite = findStalactite(local.block)
        if ((stalactite != null) && (!_placedBlocks.contains(stalactite))) {
          stalactite.drop()
        }
      }
    }

  }

  @EventHandler
  fun onBlockPlace(event: BlockPlaceEvent) {
    if (!isEnabled()) {
      return
    }
    val block = event.getBlockPlaced()
    if (block.type == Material.POINTED_DRIPSTONE) {
      val stalactite = Stalactite.fromPart(block)
      _placedBlocks.add(stalactite, (TICKS_PER_SECOND * 2).toLong())
    }
  }

  fun register() {
    this.runTaskTimer(plugin, 1L, 4L)
  }

}
