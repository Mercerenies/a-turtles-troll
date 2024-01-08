
package com.mercerenies.turtletroll.prisoner

fun interface PrisonerRewardSupplier {

  companion object {

    fun uniform(vararg rewards: PrisonerReward) = PrisonerRewardSupplier {
      rewards.random()
    }

  }

  fun chooseReward(): PrisonerReward

}
