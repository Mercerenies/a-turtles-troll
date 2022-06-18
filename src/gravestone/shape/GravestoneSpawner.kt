
package com.mercerenies.turtletroll.gravestone.shape

import com.mercerenies.turtletroll.gravestone.*

import org.bukkit.block.Block
import org.bukkit.block.BlockFace
import org.bukkit.block.`data`.`type`.Stairs
import org.bukkit.block.`data`.`type`.Slab
import org.bukkit.block.`data`.Bisected
import org.bukkit.Material

abstract class GravestoneSpawner {

  companion object GravestoneSpawner {

    fun isBlacklisted(block: Block): Boolean =
      block.type != Material.AIR

    fun replaceWith(block: Block, type: Material) {
      if (!isBlacklisted(block)) {
        block.type = type
      }
    }

    fun replaceWithStone(block: Block) {
      replaceWith(block, Material.STONE)
    }

    fun replaceWithStair(block: Block, half: Bisected.Half, face: BlockFace) {
      val type = Material.STONE_STAIRS
      if (!isBlacklisted(block)) {
        block.type = type
        val dat = block.getBlockData() as Stairs
        dat.setFacing(face)
        dat.setHalf(half)
        block.setBlockData(dat)
      }
    }

    fun replaceWithSlab(block: Block, half: Slab.Type) {
      val type = Material.STONE_SLAB
      if (!isBlacklisted(block)) {
        block.type = type
        val dat = block.getBlockData()
        if (dat is Slab) {
          dat.setType(half)
        }
        block.setBlockData(dat)
      }
    }

    fun replaceWithSlab(block: Block, half: Bisected.Half) {
      // Convenience delegator
      val slabType = (if (half == Bisected.Half.BOTTOM) Slab.Type.BOTTOM else Slab.Type.TOP)
      replaceWithSlab(block, slabType)
    }

  }

  abstract fun spawnGravestone(centerBlock: Block, inscriptions: Inscriptions, rotation: Rotation)

}
