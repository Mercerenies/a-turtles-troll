
package com.mercerenies.turtletroll.candy

import org.bukkit.entity.Player

// Decorator class which adds Candy Shop coin functionality directly
// to a Player object.
class CandyShopPlayerDecorator(
  private val coinAccessor: CandyShopCoinAccessor,
  val player: Player,
) {
  var coins: Int
    get() = coinAccessor.getCoins(player)
    set(value) = coinAccessor.setCoins(player, value)
}
