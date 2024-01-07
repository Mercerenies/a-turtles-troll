
package com.mercerenies.turtletroll.falling

import org.bukkit.Material
import org.bukkit.block.Block

import kotlin.random.Random

class SandAttackBlockDropper(
  override val maxDropHeight: Int,
  val redSandChance: Double,
) : BlockDropper() {

  override fun canDropThroughBlock(block: Block): Boolean = true

  override fun getBlockToDrop() =
    if (Random.nextDouble() < redSandChance) {
      Material.RED_SAND
    } else {
      Material.SAND
    }

}
