
package com.mercerenies.turtletroll.trivia.question

import com.mercerenies.turtletroll.AllItems

import org.bukkit.inventory.ItemStack
import org.bukkit.Material
import org.bukkit.entity.Player

import net.kyori.adventure.text.Component

// A bow and some number of arrows.
class BowAndArrowReward(
  private val arrowCount: Int,
) : TriviaQuestionReward {

  override val name: Component =
    Component.text("Bow and Arrow")

  override fun rewardPlayer(player: Player) {
    AllItems.give(player, ItemStack(Material.BOW))
    AllItems.give(player, ItemStack(Material.ARROW, arrowCount))
  }

}
