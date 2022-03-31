
package com.mercerenies.turtletroll.gravestone

import com.mercerenies.turtletroll.ext.*

import org.bukkit.block.BlockFace
import org.bukkit.util.Vector

enum class Rotation(private val count: Int) {
  NONE(0),
  NINETY(1),
  ONE_EIGHTY(2),
  TWO_SEVENTY(3);

  companion object {

    private fun rotBlockFace(face: BlockFace) =
      when (face) {
        BlockFace.EAST -> BlockFace.SOUTH
        BlockFace.NORTH -> BlockFace.EAST
        BlockFace.WEST -> BlockFace.NORTH
        BlockFace.SOUTH -> BlockFace.WEST
        else -> face // Only implemented for base four faces
      }

    private fun rotVector(v: Vector) =
      Vector(-v.z, v.y, v.x)

    private fun<T> nTimes(n: Int, start: T, function: (T) -> T): T {
      var value = start
      for (i in 1..n) {
        value = function(value)
      }
      return value
    }

    fun random() =
      listOf(NONE, NINETY, ONE_EIGHTY, TWO_SEVENTY).sample()!!

  }

  fun blockFace(face: BlockFace) =
    nTimes(count, face, ::rotBlockFace)

  fun vector(v: Vector) =
    nTimes(count, v, ::rotVector)

  fun vector(x: Int, y: Int, z: Int) =
    vector(Vector(x, y, z))

}
