
package com.mercerenies.turtletroll.temperature

import org.bukkit.entity.Player

object WearingBasicOutfitCondition : BiomeSafetyCondition {

  override fun evaluate(player: Player): BiomeSafetyCondition.Result =
    if (isWearingEnoughArmor(player)) {
      BiomeSafetyCondition.safe("You are wearing a chestplate and leggings")
    } else {
      BiomeSafetyCondition.unsafe("You are NOT wearing a chestplate and leggings")
    }

  private fun isWearingEnoughArmor(player: Player): Boolean {
    val inv = player.inventory
    return ((inv.chestplate != null) && (inv.leggings != null))
  }

}
