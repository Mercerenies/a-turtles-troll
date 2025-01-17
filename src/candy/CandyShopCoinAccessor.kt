
package com.mercerenies.turtletroll.candy

import com.mercerenies.turtletroll.util.component.*

import org.bukkit.plugin.Plugin
import org.bukkit.entity.Player
import org.bukkit.persistence.PersistentDataType
import org.bukkit.NamespacedKey

import kotlin.math.max

class CandyShopCoinAccessor(
  plugin: Plugin,
) {
  companion object {
    val COIN_MARKER_KEY = "candy_shop_coin_tag"

    fun getMarkerKey(plugin: Plugin): NamespacedKey =
      NamespacedKey(plugin, COIN_MARKER_KEY)
  }

  val markerKey = getMarkerKey(plugin)

  fun getCoins(player: Player): Int {
    val container = player.persistentDataContainer
    return container.getOrDefault(markerKey, PersistentDataType.INTEGER, 0)
  }

  fun setCoins(player: Player, coinCount: Int) {
    val container = player.persistentDataContainer
    container.set(markerKey, PersistentDataType.INTEGER, max(coinCount, 0))
  }

  fun wrap(player: Player): CandyShopPlayerDecorator =
    CandyShopPlayerDecorator(this, player)
}
