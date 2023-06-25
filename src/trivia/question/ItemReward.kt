
package com.mercerenies.turtletroll.trivia.question

import com.mercerenies.turtletroll.AllItems

import org.bukkit.inventory.ItemStack
import org.bukkit.Material
import org.bukkit.entity.Player

import net.kyori.adventure.text.Component

class ItemReward(
  private val itemStack: ItemStack
) : TriviaQuestionReward {

  constructor(material: Material) : this(ItemStack(material))

  override val name: Component
    get() = AllItems.getName(itemStack)

  override fun rewardPlayer(player: Player) {
    AllItems.give(player, itemStack.clone())
  }

}
