
package com.mercerenies.turtletroll.temperature

import org.bukkit.entity.Player
import org.bukkit.Material

class HoldingItemSafetyCondition(
  val itemDescription: String,
  val items: Set<Material>
) : BiomeSafetyCondition {

  override fun evaluate(player: Player): BiomeSafetyCondition.Result {
    val mainHand = player.inventory.itemInMainHand.type
    val offHand = player.inventory.itemInOffHand.type
    if (items.contains(mainHand) || items.contains(offHand)) {
      return BiomeSafetyCondition.safe("You are holding $itemDescription")
    } else {
      return BiomeSafetyCondition.unsafe("You are NOT holding $itemDescription")
    }
  }

}
