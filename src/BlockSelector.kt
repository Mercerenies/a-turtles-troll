
package com.mercerenies.turtletroll

import org.bukkit.Chunk
import org.bukkit.block.Block

import kotlin.random.Random

// Various helper functions for getting locations
object BlockSelector {

  val SEA_LEVEL = 64

  fun getRandomBlock(chunk: Chunk, max_y: Int = 256): Block {
    val x = Random.nextInt(16)
    val y = Random.nextInt(max_y)
    val z = Random.nextInt(16)
    return chunk.getBlock(x, y, z)
  }

  fun getRandomBlockNear(block: Block): Block {
    val x = block.getX() + Random.nextInt(-15, 16)
    val y = Random.nextInt(256)
    val z = block.getZ() + Random.nextInt(-15, 16)
    return block.location.world!!.getBlockAt(x, y, z)
  }

}
