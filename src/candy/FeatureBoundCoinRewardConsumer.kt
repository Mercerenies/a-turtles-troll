
package com.mercerenies.turtletroll.candy

import com.mercerenies.turtletroll.feature.HasEnabledStatus

import org.bukkit.entity.Player

class FeatureBoundCoinRewardConsumer<T : CoinRewardConsumer>(
  private val inner: CoinRewardConsumer,
  private val feature: HasEnabledStatus,
) : CoinRewardConsumer {
  override fun rewardCoins(player: Player, reason: String, amount: Int) {
    if (feature.isEnabled()) {
      inner.rewardCoins(player, reason, amount)
    }
  }
}

fun<T : CoinRewardConsumer> T.boundToFeature(feature: HasEnabledStatus): FeatureBoundCoinRewardConsumer<T> =
  FeatureBoundCoinRewardConsumer(this, feature)
