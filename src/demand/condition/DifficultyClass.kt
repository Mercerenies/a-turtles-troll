
package com.mercerenies.turtletroll.demand.condition

import com.mercerenies.turtletroll.demand.DeathCondition

// TODO Validate precondition (conditions is not empty) here, and
// provide a function to sample (with the non-null assertion)
data class DifficultyClass(
  val conditions: List<DeathCondition>,
) {
  companion object {

    val EASY = DifficultyClass(
      listOf(
        DeathCondition.True, DeathCondition.MustBeMimic,
        DeathCondition.MustBeVector, DeathCondition.MustBeAngel,
        DeathCondition.FireDamage, DeathCondition.Falling,
        DeathCondition.MustBeBee, DeathCondition.MustBeSilverfish,
        DeathCondition.MustBeRedstone, DeathCondition.Lightning,
      )
    )

    val MEDIUM = DifficultyClass(
      listOf(
        DeathCondition.Explosion, DeathCondition.MustBeZombie,
        DeathCondition.MustBeGhast, DeathCondition.MustBeRavager,
        DeathCondition.Hunger, DeathCondition.MustBeSpider,
      )
    )

    val HARD = DifficultyClass(
      listOf(
        DeathCondition.Drowning, DeathCondition.MustBeEnderman,
        DeathCondition.MustBeIronGolem, DeathCondition.MustBeBlaze,
        DeathCondition.MustBeLlama,
      )
    )

  }
}
