
package com.mercerenies.turtletroll.drop

import com.mercerenies.turtletroll.Infestation

import org.bukkit.Material
import org.bukkit.block.Block
import org.bukkit.entity.EntityType

import kotlin.random.Random

data class SilverfishAttackBomb(
  val probability: Double,
  val radius: Int,
  val releaseChance: Double,
  val infestationChance: Double,
) {

  fun rollForBlock(random: Random = Random.Default): BombBehavior {
    val rnd = random.nextDouble()
    return when {
      rnd < releaseChance -> SpawnSilverfish
      rnd < releaseChance + infestationChance -> InfestBlock
      else -> NoBehavior
    }
  }

  companion object {

    val DEFAULT = SilverfishAttackBomb(
      probability = 0.2,
      radius = 3,
      releaseChance = 0.2,
      infestationChance = 0.4,
    )

    val HARDCORE = SilverfishAttackBomb(
      probability = 1.0,
      radius = 10,
      releaseChance = 0.5,
      infestationChance = 0.5,
    )

    fun interface BombBehavior {
      fun run(block: Block)
    }

    object NoBehavior : BombBehavior {
      override fun run(block: Block) {}
    }

    object SpawnSilverfish : BombBehavior {
      override fun run(block: Block) {
        block.type = Material.AIR
        block.world.spawnEntity(block.location.add(0.5, 0.5, 0.5), EntityType.SILVERFISH)
      }
    }

    object InfestBlock : BombBehavior {
      override fun run(block: Block) {
        Infestation.infest(block)
      }
    }

  }

}
