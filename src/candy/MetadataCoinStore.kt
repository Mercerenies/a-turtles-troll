
package com.mercerenies.turtletroll.candy

import com.mercerenies.turtletroll.util.component.*

import org.bukkit.plugin.Plugin
import org.bukkit.entity.Player
import org.bukkit.persistence.PersistentDataType
import org.bukkit.NamespacedKey

import kotlin.math.max

class MetadataCoinStore(
  plugin: Plugin,
) : CoinStore {
  companion object {
    val COIN_MARKER_KEY = "candy_shop_coin_tag"

    fun getMarkerKey(plugin: Plugin): NamespacedKey =
      NamespacedKey(plugin, COIN_MARKER_KEY)
  }

  val markerKey = getMarkerKey(plugin)

  override fun getCoins(player: Player): Int {
    val container = player.persistentDataContainer
    return container.getOrDefault(markerKey, PersistentDataType.INTEGER, 0)
  }

  override fun setCoins(player: Player, coinCount: Int) {
    val container = player.persistentDataContainer
    container.set(markerKey, PersistentDataType.INTEGER, max(coinCount, 0))
  }
}
