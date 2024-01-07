
package com.mercerenies.turtletroll.location

import com.mercerenies.turtletroll.mimic.MimicIdentifier
import com.mercerenies.turtletroll.cake.CakeListener

import org.bukkit.Chunk
import org.bukkit.Location
import org.bukkit.block.Block

import kotlin.random.Random
import kotlin.collections.ArrayList
import kotlin.math.ceil

// Various helper functions for getting locations relative to blocks
object BlockSelector {

  data class Column(
    val x: Int,
    val z: Int,
  )

  val BEDROCK = -64
  val SEA_LEVEL = 64

  fun getRandomBlock(chunk: Chunk, min_y: Int = -64, max_y: Int = 256): Block {
    val x = Random.nextInt(16)
    val y = Random.nextInt(min_y, max_y)
    val z = Random.nextInt(16)
    return chunk.getBlock(x, y, z)
  }

  fun getRandomColumn(chunk: Chunk): Column {
    val x = Random.nextInt(16)
    val z = Random.nextInt(16)
    return Column(16 * chunk.x + x, 16 * chunk.z + z)
  }

  fun getRandomBlockNear(block: Block): Block {
    val x = block.getX() + Random.nextInt(-15, 16)
    val y = Random.nextInt(-64, 256)
    val z = block.getZ() + Random.nextInt(-15, 16)
    return block.location.world!!.getBlockAt(x, y, z)
  }

  fun getRandomBlockNearDims(block: Block, distance: Int = 16): Block {
    val x = block.getX() + Random.nextInt(- distance - 1, distance)
    val y = block.getY() + Random.nextInt(- distance - 1, distance)
    val z = block.getZ() + Random.nextInt(- distance - 1, distance)
    return block.location.world!!.getBlockAt(x, y, z)
  }

  fun getNearby(location: Location, distance: Int): List<Location> {
    val result = ArrayList<Location>()
    for (z in -distance..distance) {
      for (y in -distance..distance) {
        for (x in -distance..distance) {
          if (Math.abs(x) + Math.abs(y) + Math.abs(z) <= distance) {
            result.add(Location(location.world, x.toDouble(), y.toDouble(), z.toDouble()).add(location))
          }
        }
      }
    }
    return result
  }

  fun getNearbyXZ(location: Location, distance: Int): List<Location> {
    val result = ArrayList<Location>()
    for (z in -distance..distance) {
      for (x in -distance..distance) {
        if (Math.abs(x) + Math.abs(z) <= distance) {
          result.add(Location(location.world, x.toDouble(), 0.0, z.toDouble()).add(location))
        }
      }
    }
    return result
  }

  fun getSphere(center: Location, radius: Double): List<Location> =
    getNearby(center, ceil(radius).toInt())
      .filter { it.distanceSquared(center) <= radius * radius }

  fun isExposedToSky(block: Block): Boolean =
    block.getLightFromSky() == 15.toByte()

  fun<T> selectNearbyMatching(block: Block, distance: Int, matcher: (Block) -> T?): List<T> {
    val matches = ArrayList<T>()
    for (dx in -distance..distance) {
      for (dy in -distance..distance) {
        for (dz in -distance..distance) {
          val testBlock = block.location.clone().add(dx.toDouble(), dy.toDouble(), dz.toDouble()).block
          val match = matcher(testBlock)
          if (match != null) {
            matches.add(match)
          }
        }
      }
    }
    return matches
  }

  fun columnsInChunk(chunkX: Int, chunkZ: Int): List<Column> {
    val columns = ArrayList<Column>()
    for (x in 0 until 16) {
      for (z in 0 until 16) {
        columns.add(Column(16 * chunkX + x, 16 * chunkZ + z))
      }
    }
    return columns
  }

  fun columnsInChunk(chunk: Chunk): List<Column> =
    columnsInChunk(chunk.x, chunk.z)

  fun countNearbyMatching(block: Block, distance: Int, condition: (Block) -> Boolean): Int {
    fun matcher(block: Block): Unit? =
      if (condition(block)) Unit else null
    return selectNearbyMatching(block, distance, ::matcher).size
  }

  // Used to detect mimics and cakes for spawn limits
  //
  // TODO I don't like that we have to explicitly take MimicIdentifier
  // here. Can this method go somewhere better where we already have
  // access to things?
  fun isMimicOrCake(mimicIdentifier: MimicIdentifier, block: Block): Boolean =
    mimicIdentifier.isMimic(block) || CakeListener.isCake(block.type)

}
