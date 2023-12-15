
package com.mercerenies.turtletroll.demand.bowser

import com.mercerenies.turtletroll.ext.*
import com.mercerenies.turtletroll.AllItems

import org.bukkit.inventory.ItemStack
import org.bukkit.entity.Player

import net.kyori.adventure.text.Component

class ItemReward(
  val mainItem: ItemStack,
  val specialItem: ItemStack,
  val mainText: Component,
  val specialText: (Player) -> Component,
) : BowserReward {

  override fun getMainRewardText(): Component =
    BowserEvent.bowserMessage(mainText)

  override fun getSpecialRewardText(player: Player): Component =
    BowserEvent.bowserMessage(specialText(player))

  override fun giveMainReward(player: Player) {
    AllItems.give(player, mainItem.clone())
  }

  override fun giveSpecialReward(player: Player) {
    AllItems.give(player, specialItem.clone())
  }

}
