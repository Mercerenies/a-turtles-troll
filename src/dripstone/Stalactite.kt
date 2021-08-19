
package com.mercerenies.turtletroll.dripstone

import org.bukkit.block.Block
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

  }

  constructor(blocks: Collection<Block>) :
    this(HashSet(blocks.map { EqBlock(it) }))

  fun drop() {
    for (block in blocks) {
      val blockData = block.block.getBlockData() as PointedDripstone
      val location = block.block.location.clone().add(0.5, 0.0, 0.5)
      block.block.type = Material.AIR
      val fallingBlock = block.block.world.spawnFallingBlock(location, blockData)
      try {
        NMS.makeFallingBlockHurt(fallingBlock)
      } catch (e: Exception) {
        println("Oh no! Something went wrong during my horrible NMS hack. Please report: ${e}")
      }
    }
  }

}
