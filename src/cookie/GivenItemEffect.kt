
package com.mercerenies.turtletroll.cookie

import com.mercerenies.turtletroll.ext.*
import com.mercerenies.turtletroll.AllItems
import com.mercerenies.turtletroll.Rarity

import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import org.bukkit.entity.Player
import org.bukkit.plugin.Plugin
import org.bukkit.scheduler.BukkitRunnable

abstract class GivenItemEffect(private val plugin: Plugin) : CookieEffect {

  companion object {

    private val DELAY = 2

    private val VOWELS = setOf('A', 'E', 'I', 'O', 'U', 'a', 'e', 'i', 'o', 'u')

    fun isVowel(char: Char): Boolean =
      VOWELS.contains(char)

    fun startsWithVowel(name: String): Boolean =
      (name != "") && isVowel(name[0])

  }

  class AnyNonEpicItem(_plugin: Plugin) : GivenItemEffect(_plugin) {

    private val candidates: List<Material> =
      AllItems.allItems.filter { AllItems.getRarity(it) != Rarity.EPIC }

    override fun chooseItem(): ItemStack {
      val material = candidates.sample()!!
      return ItemStack(material, 1)
    }

  }

  class AnotherCookie(_plugin: Plugin) : GivenItemEffect(_plugin) {

    override fun itemName(item: ItemStack): String =
      "another cookie"

    override fun chooseItem(): ItemStack =
      ItemStack(Material.COOKIE, 1)

  }

  class TwoMoreCookies(_plugin: Plugin) : GivenItemEffect(_plugin) {

    override fun itemName(item: ItemStack): String =
      "two more cookies"

    override fun chooseItem(): ItemStack =
      ItemStack(Material.COOKIE, 2)

  }

  private class GiveToPlayer(val player: Player, val itemStack: ItemStack) : BukkitRunnable() {
    override fun run() {
      AllItems.give(player, itemStack)
    }
  }

  abstract fun chooseItem(): ItemStack?

  open val fallback: CookieEffect
    get() = NoEffect

  open fun itemName(item: ItemStack): String {
    val itemName = AllItems.getName(item)
    val article = if (startsWithVowel(itemName)) "an" else "a"
    return "${article} ${itemName}"
  }

  open override fun cancelsDefault(): Boolean = false

  override fun onEat(stack: ItemStack, player: Player) {
    val replacementItem = chooseItem()
    if (replacementItem == null) {
      fallback.onEat(stack, player)
    } else {
      val itemName = this.itemName(replacementItem)
      player.sendMessage("That cookie had ${itemName} inside it!")
      GiveToPlayer(player, replacementItem).runTaskLater(plugin, DELAY.toLong())
    }
  }

}
