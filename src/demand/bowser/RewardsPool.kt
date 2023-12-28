
package com.mercerenies.turtletroll.demand.bowser

import com.mercerenies.turtletroll.util.component.*
import com.mercerenies.turtletroll.util.withEnchantment

import org.bukkit.inventory.ItemStack
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.enchantments.Enchantment

import net.kyori.adventure.text.Component

object RewardsPool {

  fun itemRewardText(itemName: String): Component =
    Component.text("Huh?! Done already! Fine, I'll get you next time! Take your ${itemName} and go!")

  fun enchantedItemText(player: Player): Component =
    Component.text("Oh, and good work, ").append(player.displayName()).append(". You can have an enchanted one.")

  fun extraItemText(player: Player): Component =
    Component.text("Oh, and for your effort, ").append(player.displayName()).append(", take a little extra!")

  val ALL_REWARDS: List<BowserReward> = listOf(
    ItemReward(
      mainItem = ItemStack(Material.DIAMOND_PICKAXE),
      specialItem = ItemStack(Material.DIAMOND_PICKAXE).withEnchantment(Enchantment.DURABILITY, 1),
      mainText = itemRewardText("Diamond Pickaxe"),
      specialText = ::enchantedItemText,
    ),
    ItemReward(
      mainItem = ItemStack(Material.COOKED_BEEF, 16),
      specialItem = ItemStack(Material.COOKED_BEEF, 32),
      mainText = itemRewardText("Steaks"),
      specialText = ::extraItemText,
    ),
  )

}
