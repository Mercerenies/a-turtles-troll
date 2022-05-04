
package com.mercerenies.turtletroll.cookie

import com.mercerenies.turtletroll.ext.*
import com.mercerenies.turtletroll.AllItems
import com.mercerenies.turtletroll.Rarity

import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import org.bukkit.entity.Player

abstract class GivenItemEffect : CookieEffect {

  companion object {

    private val VOWELS = setOf('A', 'E', 'I', 'O', 'U', 'a', 'e', 'i', 'o', 'u')

    fun isVowel(char: Char): Boolean =
      VOWELS.contains(char)

    fun startsWithVowel(name: String): Boolean =
      (name != "") && isVowel(name[0])

  }

  object AnyNonEpicItem : GivenItemEffect() {

    private val candidates: List<Material> =
      AllItems.allItems.filter { AllItems.getRarity(it) != Rarity.EPIC }

    override fun chooseItem(): ItemStack {
      val material = candidates.sample()!!
      return ItemStack(material, 1)
    }

  }

  object AnotherCookie : GivenItemEffect() {

    override fun itemName(item: ItemStack): String =
      "another cookie"

    override fun chooseItem(): ItemStack =
      ItemStack(Material.COOKIE, 1)

  }

  object TwoMoreCookies : GivenItemEffect() {

    override fun itemName(item: ItemStack): String =
      "two more cookies"

    override fun chooseItem(): ItemStack =
      ItemStack(Material.COOKIE, 2)

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
      AllItems.give(player, replacementItem)
    }
  }

}
