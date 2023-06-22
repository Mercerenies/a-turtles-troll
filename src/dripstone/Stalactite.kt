
package com.mercerenies.turtletroll.dripstone

import org.bukkit.block.Block
import org.bukkit.block.BlockFace
import org.bukkit.Material
import org.bukkit.Location
import org.bukkit.block.`data`.type.PointedDripstone

import com.mercerenies.turtletroll.nms.NMS

// A collection of blocks forming a stalactite of dripstone. This
// class provides helpers for extending or dropping the stalactite.
data class Stalactite(val blocks: Set<EqBlock>) {

  companion object {

    fun fromPart(bottomBlock: Block): Stalactite {
      val arr = ArrayList<Block>()
      var loc: Location

      // Go up
      loc = bottomBlock.location.clone()
      while (true) {
        val innerBlock = loc.block
        if (innerBlock.type != Material.POINTED_DRIPSTONE) {
          break
        }
        arr.add(innerBlock)
        loc.setY(loc.getY() + 1)
      }

      // Go down
      loc = bottomBlock.location.clone()
      while (true) {
        val innerBlock = loc.block
        if (innerBlock.type != Material.POINTED_DRIPSTONE) {
          break
        }
        arr.add(innerBlock)
        loc.setY(loc.getY() - 1)
      }

      return Stalactite(arr)
    }

    // From bottom to top
    fun thicknessForLength(length: Int): List<PointedDripstone.Thickness> {
      when (length) {
        0 -> return listOf()
        1 -> return listOf(PointedDripstone.Thickness.TIP)
        2 -> return listOf(PointedDripstone.Thickness.TIP, PointedDripstone.Thickness.FRUSTUM)
        else -> {
          val result = ArrayList<PointedDripstone.Thickness>()
          result.add(PointedDripstone.Thickness.TIP)
          result.add(PointedDripstone.Thickness.FRUSTUM)
          repeat(length - 3) {
            result.add(PointedDripstone.Thickness.MIDDLE)
          }
          result.add(PointedDripstone.Thickness.BASE)
          return result
        }
      }
    }

  }

  // Blocks sorted by Y coordinate (from lowest to highest)
  val sortedBlocks: List<Block>
    get() = blocks.map { it.block }.sortedBy { it.location.getY() }

  constructor(blocks: Collection<Block>) :
    this(HashSet(blocks.map { EqBlock(it) }))

  // Update the thickness (called when the stalactite grows)
  fun updateData() {
    val sortedBlocks = this.sortedBlocks
    for ((block, thickness) in sortedBlocks.zip(thicknessForLength(sortedBlocks.size))) {
      val blockData = block.getBlockData() as PointedDripstone
      blockData.setThickness(thickness)
      blockData.setVerticalDirection(BlockFace.DOWN)
      block.setBlockData(blockData)
    }
  }

  fun drop() {
    for (block in blocks) {
      val blockData = block.block.getBlockData() as PointedDripstone
      val location = block.block.location.clone().add(0.5, 0.0, 0.5)
      block.block.type = Material.AIR
      val fallingBlock = block.block.world.spawnFallingBlock(location, blockData)
      NMS.makeFallingBlockHurt(fallingBlock)
    }
  }

  fun grow(): Stalactite {
    val bottom = this.sortedBlocks[0]
    val loc = bottom.location.clone().add(0.0, -1.0, 0.0)
    loc.block.type = Material.POINTED_DRIPSTONE
    val blockData = loc.block.getBlockData() as PointedDripstone
    blockData.setVerticalDirection(BlockFace.DOWN)
    loc.block.setBlockData(blockData)
    val newStalactite = fromPart(loc.block)
    newStalactite.updateData()
    return newStalactite
  }

}
