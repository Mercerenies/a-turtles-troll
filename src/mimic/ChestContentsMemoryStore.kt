
package com.mercerenies.turtletroll.mimic

import org.bukkit.inventory.ItemStack
import org.bukkit.entity.Player

import kotlin.collections.HashMap

class ChestContentsMemoryStore() : ChestContentsStore {

  private val map = HashMap<Player, Array<ItemStack?>>()

  fun setContents(player: Player, stack: Array<ItemStack?>) {
    map[player] = stack
  }

  override fun getContents(player: Player): Array<ItemStack?>? =
    map[player]

  override val keys: Set<Player>
    get() = map.keys

}
