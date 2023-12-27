
package com.mercerenies.turtletroll.mimic

import org.bukkit.inventory.ItemStack
import org.bukkit.entity.Player

interface ChestContentsStore {

  fun getContents(player: Player): Array<ItemStack?>?

  val keys: Set<Player>

}
