
package com.mercerenies.turtletroll.candy

import org.bukkit.entity.Player

interface CoinRewardConsumer {
  // `reason` shall be a clause which grammatically makes sense at the
  // end of "Got coins because XXX".
  fun rewardCoins(player: Player, reason: String, amount: Int)

  object Null : CoinRewardConsumer {
    override fun rewardCoins(player: Player, reason: String, amount: Int) {
      // Do nothing
    }
  }
}
