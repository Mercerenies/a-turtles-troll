
package com.mercerenies.turtletroll.drop.nearby

import org.bukkit.Material
import org.bukkit.event.block.BlockBreakEvent

class StrongholdSilverfishAttackAction(
  _bombData: SilverfishAttackBomb = DEFAULT_BOMB
) : SilverfishAttackAction(_bombData) {

  override fun shouldTrigger(event: BlockBreakEvent): Boolean =
    super.shouldTrigger(event) && BLOCKS.contains(event.block.type)

  companion object {

    val DEFAULT_BOMB = SilverfishAttackBomb(
      probability = 1.0,
      radius = 4,
      releaseChance = 0.5,
      infestationChance = 0.5,
    )

    val BLOCKS = setOf(
      Material.STONE_BRICKS, Material.CRACKED_STONE_BRICKS, Material.MOSSY_STONE_BRICKS,
    )

  }

}
