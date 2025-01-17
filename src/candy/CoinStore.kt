
package com.mercerenies.turtletroll.candy

import com.mercerenies.turtletroll.util.component.*

import org.bukkit.entity.Player

interface CoinStore {
  fun getCoins(player: Player): Int
  fun setCoins(player: Player, coinCount: Int)
}

fun CoinStore.wrap(player: Player): CoinStorePlayerDecorator =
  CoinStorePlayerDecorator(this, player)
