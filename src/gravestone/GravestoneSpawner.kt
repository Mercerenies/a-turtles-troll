
package com.mercerenies.turtletroll.gravestone

import org.bukkit.block.Block
import org.bukkit.block.BlockFace
import org.bukkit.block.Sign
import org.bukkit.block.`data`.`type`.Bed
import org.bukkit.block.`data`.`type`.WallSign
import org.bukkit.Material

object GravestoneSpawner {

  val BLACKLISTED_BLOCK_TYPES = setOf(
    Material.BEDROCK, Material.END_PORTAL, Material.END_PORTAL_FRAME,
    Material.OBSIDIAN, Material.NETHER_PORTAL,
  )

  fun isBlacklisted(block: Block): Boolean =
    BLACKLISTED_BLOCK_TYPES.contains(block.type) || (block.blockData is Bed)

  fun replaceWith(block: Block, type: Material) {
    if (!isBlacklisted(block)) {
      block.type = type
    }
  }

  fun spawnGravestone(centerBlock: Block, inscriptions: Inscriptions) {

    // The stone itself
    replaceWith(centerBlock, Material.STONE)
    replaceWith(centerBlock.location.clone().add(0.0, 1.0,  0.0).block, Material.STONE)
    replaceWith(centerBlock.location.clone().add(0.0, 2.0,  0.0).block, Material.STONE)
    replaceWith(centerBlock.location.clone().add(0.0, 0.0,  1.0).block, Material.STONE)
    replaceWith(centerBlock.location.clone().add(0.0, 1.0,  1.0).block, Material.STONE)
    replaceWith(centerBlock.location.clone().add(0.0, 0.0, -1.0).block, Material.STONE)
    replaceWith(centerBlock.location.clone().add(0.0, 1.0, -1.0).block, Material.STONE)

    // Now the sign
    val signBlock = centerBlock.location.clone().add(1.0, 1.0, 0.0).block
    replaceWith(signBlock, Material.BIRCH_WALL_SIGN)

    val blockData = signBlock.blockData
    if (blockData is WallSign) {
      blockData.setFacing(BlockFace.EAST)
    }
    signBlock.blockData = blockData

    val blockState = signBlock.state
    if (blockState is Sign) {
      for (index in 0..3) {
        blockState.setLine(index, inscriptions.getLine(index))
      }
    }
    blockState.update()

  }

}
