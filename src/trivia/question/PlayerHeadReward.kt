
package com.mercerenies.turtletroll.trivia.question

import com.mercerenies.turtletroll.AllItems

import org.bukkit.entity.Player
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.inventory.meta.SkullMeta
import org.bukkit.inventory.ItemStack

import net.kyori.adventure.text.Component

class PlayerHeadReward(
  private val playerName: String,
) : TriviaQuestionReward {

  private val offlinePlayer by lazy {
    Bukkit.getOfflinePlayer(playerName)
  }

  override val name: Component =
    Component.text(playerName)

  override fun rewardPlayer(player: Player) {
    val itemStack = ItemStack(Material.PLAYER_HEAD)
    val meta = itemStack.itemMeta as SkullMeta
    meta.setOwningPlayer(offlinePlayer)
    itemStack.itemMeta = meta
    AllItems.give(player, itemStack)
  }

}
