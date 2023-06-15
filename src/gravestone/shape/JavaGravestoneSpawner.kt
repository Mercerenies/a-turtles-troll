
package com.mercerenies.turtletroll.gravestone.shape

import com.mercerenies.turtletroll.gravestone.*

import org.bukkit.block.Block
import org.bukkit.block.BlockFace
import org.bukkit.block.Sign
import org.bukkit.block.`data`.`type`.WallSign
import org.bukkit.block.`data`.Bisected
import org.bukkit.Material
import org.bukkit.block.sign.Side

object JavaGravestoneSpawner : GravestoneSpawner() {

  override fun spawnGravestone(centerBlock: Block, inscriptions: Inscriptions, rotation: Rotation) {

    // The stone itself
    replaceWithSlab(centerBlock, Bisected.Half.TOP)
    replaceWithStone(centerBlock.location.clone().add(rotation.vector(0, 1, 0)).block)
    replaceWithSlab(centerBlock.location.clone().add(rotation.vector(0, 2, 0)).block, Bisected.Half.BOTTOM)
    replaceWithStair(centerBlock.location.clone().add(rotation.vector(0, 0, 1)).block, Bisected.Half.BOTTOM, BlockFace.SOUTH)
    // replaceWithStair(centerBlock.location.clone().add(rotation.vector(0, 1, 1)).block, Bisected.Half.TOP, BlockFace.SOUTH)
    replaceWithStair(centerBlock.location.clone().add(rotation.vector(0, 2, 1)).block, Bisected.Half.TOP, BlockFace.SOUTH)
    replaceWithStair(centerBlock.location.clone().add(rotation.vector(0, 0, -1)).block, Bisected.Half.BOTTOM, BlockFace.NORTH)
    // replaceWithStair(centerBlock.location.clone().add(rotation.vector(0, 1, -1)).block, Bisected.Half.TOP, BlockFace.NORTH)
    replaceWithStair(centerBlock.location.clone().add(rotation.vector(0, 2, -1)).block, Bisected.Half.TOP, BlockFace.NORTH)

    // Now the sign
    val signBlock = centerBlock.location.clone().add(rotation.vector(1, 1, 0)).block
    replaceWith(signBlock, Material.BIRCH_WALL_SIGN)

    val blockData = signBlock.blockData
    if (blockData is WallSign) {
      blockData.setFacing(rotation.blockFace(BlockFace.EAST))
    }
    signBlock.blockData = blockData

    val blockState = signBlock.state
    if (blockState is Sign) {
      inscriptions.printTo(blockState.getSide(Side.FRONT))
    }
    blockState.update()

  }

}
