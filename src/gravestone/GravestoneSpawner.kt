
package com.mercerenies.turtletroll.gravestone

import org.bukkit.block.Block
import org.bukkit.block.BlockFace
import org.bukkit.block.Sign
import org.bukkit.block.`data`.`type`.Stairs
import org.bukkit.block.`data`.`type`.Bed
import org.bukkit.block.`data`.`type`.WallSign
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

    fun replaceWithStair(block: Block, type: Material, half: Bisected.Half, face: BlockFace) {
      if (!isBlacklisted(block)) {
        block.type = type
        val dat = block.getBlockData()
        if (dat is Stairs) {
          dat.setFacing(face)
          dat.setHalf(half)
        }
        block.setBlockData(dat)
      }
    }

  }

  abstract fun spawnGravestone(centerBlock: Block, inscriptions: Inscriptions, rotation: Rotation)

}
