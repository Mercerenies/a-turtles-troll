
package com.mercerenies.turtletroll.candy

import org.bukkit.entity.Player

// Decorator class which adds Candy Shop coin functionality directly
// to a Player object.
class CoinStorePlayerDecorator(
  private val coinStore: CoinStore,
  val player: Player,
) {
  var coins: Int
    get() = coinStore.getCoins(player)
    set(value) = coinStore.setCoins(player, value)
}
