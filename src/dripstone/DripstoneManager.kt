
package com.mercerenies.turtletroll.dripstone

import com.mercerenies.turtletroll.feature.RunnableFeature
import com.mercerenies.turtletroll.CooldownMemory
import com.mercerenies.turtletroll.BlockSelector
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

    fun findImmediatelyAboveStalactite(block: Block): Stalactite? {
      val loc = block.location.clone().add(0.0, 1.0, 0.0)
      if (loc.block.type == Material.POINTED_DRIPSTONE) {
        return Stalactite.fromPart(loc.block)
      } else {
        return null
      }
    }

  }

  private val _placedBlocks = CooldownMemory<Stalactite>(plugin)

  override val name = "dripstone"

  override val description = "Dripstone generates randomly and falls if you walk under it"

  private fun tryGrowExisting(onlinePlayers: List<Player>) {
    repeat(100) {
      val player = onlinePlayers.sample()
      if (player != null) {
        val playerPosBlock = player.location.block
        val randBlock = BlockSelector.getRandomBlockNear(playerPosBlock)
        // Only applies to air
        if (randBlock.type == Material.AIR) {
          // Check for existing
          val existingStalactite = findImmediatelyAboveStalactite(randBlock)
          if (existingStalactite != null) {
            existingStalactite.grow()
            _placedBlocks.add(existingStalactite, (TICKS_PER_SECOND * 2).toLong())
            return
          }
        }
      }
    }
  }

  private fun tryGrowNew(onlinePlayers: List<Player>) {
    repeat(50) {
      val player = onlinePlayers.sample()
      if (player != null) {
        val playerPosBlock = player.location.block
        val randBlock = BlockSelector.getRandomBlockNear(playerPosBlock)
        // Only applies to air
        if (randBlock.type == Material.AIR) {
          // Check for solid block (to start a new stalactite)
          val aboveBlock = randBlock.location.clone().add(0.0, 1.0, 0.0).block
          if (aboveBlock.getType().isSolid()) {
            randBlock.type = Material.POINTED_DRIPSTONE
            val newStalactite = Stalactite.fromPart(randBlock)
            newStalactite.updateData()
            _placedBlocks.add(newStalactite, (TICKS_PER_SECOND * 2).toLong())
            return
          }
        }
      }
    }
  }

  override fun run() {
    if (!isEnabled()) {
      return
    }
    val onlinePlayers = Bukkit.getOnlinePlayers().toList()
    tryGrowExisting(onlinePlayers)
    tryGrowNew(onlinePlayers)
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
    this.runTaskTimer(plugin, 1L, 10L)
  }

}
