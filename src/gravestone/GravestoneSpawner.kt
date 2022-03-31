
package com.mercerenies.turtletroll.gravestone

import org.bukkit.block.Block
import org.bukkit.block.BlockFace
import org.bukkit.block.Sign
import org.bukkit.block.`data`.`type`.Bed
import org.bukkit.block.`data`.`type`.WallSign
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

  }

  abstract fun spawnGravestone(centerBlock: Block, inscriptions: Inscriptions, rotation: Rotation)

}
