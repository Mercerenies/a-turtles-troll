
package com.mercerenies.turtletroll.candy

import com.mercerenies.turtletroll.Messages
import com.mercerenies.turtletroll.util.component.*

import org.bukkit.entity.Player

import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.Component

class CandyShopCoinConsumer(
  private val coinStore: CoinStore,
) : CoinRewardConsumer {

  override fun rewardCoins(player: Player, reason: String, amount: Int) {
    val coinPhrase = if (amount == 1) "1 coin" else "$amount coins"
    val message = Component.text()
      .append(player.displayName())
      .append(" earned ")
      .append(Component.text(coinPhrase, NamedTextColor.GREEN))
      .append(" because ")
      .append(reason)
      .build()
    Messages.broadcastMessage(message)
  }
}
