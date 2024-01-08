
package com.mercerenies.turtletroll.prisoner

import com.mercerenies.turtletroll.util.component.*
import com.mercerenies.turtletroll.AllItems

import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

import net.kyori.adventure.text.Component

open class ItemReward(
  val halfItem: ItemStack,
) : PrisonerReward {

  companion object {

    fun makePlural(quantity: Int, originalName: Component): Component =
      if (quantity == 1) {
        originalName
      } else {
        originalName.append("s")
      }

  }

  constructor(itemType: Material) :
    this(ItemStack(itemType))

  constructor(itemType: Material, itemCount: Int) :
    this(ItemStack(itemType, itemCount))

  val halfQuantity: Int = halfItem.amount
  val fullQuantity: Int = halfItem.amount * 2

  open override val halfRewardName: Component =
    Component.text(fullQuantity.toString()).append(" ").append(makePlural(halfQuantity, AllItems.getName(halfItem)))

  open override val fullRewardName: Component =
    Component.text(fullQuantity.toString()).append(" ").append(makePlural(fullQuantity, AllItems.getName(halfItem)))

  override fun awardFull(player: Player) {
    // Just do awardHalf twice; two halves make a whole.
    awardHalf(player)
    awardHalf(player)
  }

  override fun awardHalf(player: Player) {
    AllItems.give(player, halfItem.clone())
  }

}
