
package com.mercerenies.turtletroll.cookie

import com.mercerenies.turtletroll.ext.*
import com.mercerenies.turtletroll.AllItems
import com.mercerenies.turtletroll.Rarity
import com.mercerenies.turtletroll.BookBuilder

import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import org.bukkit.entity.Player

class FortuneEffect(
  private val authors: List<String>,
  private val fortunes: List<String>,
) : CookieEffect {

  companion object {

    val DEFAULT_AUTHORS = listOf(
      "Albert Einstein", "Mahatma Gandhi", "Jesus Christ", "Martin Luther King, Jr.",
      "Julius Caesar", "Ulysses S. Grant", "Abraham Lincoln", "George Washington",
      "Barack Obama", "Thomas Jefferson", "Peter Capaldi", "Benedict Cumberbatch",
    )

    val DEFAULT_FORTUNES = listOf(
      "Example Fortune :)",
    )

    val Default = FortuneEffect(DEFAULT_AUTHORS, DEFAULT_FORTUNES)

  }

  init {
    if ((authors.size == 0) || (fortunes.size == 0)) {
      throw IllegalArgumentException("Empty list given to FortuneEffect")
    }
  }

  private fun chooseAuthor(): String =
    authors.sample()!!

  private fun chooseFortune(): String =
    fortunes.sample()!!

  private fun makeBook(fortune: String): ItemStack {
    val title = "Your Fortune"
    val author = chooseAuthor()
    return BookBuilder.makeBook(title, author, listOf(fortune))
  }

  override fun cancelsDefault(): Boolean = false

  override fun onEat(stack: ItemStack, player: Player) {
    val fortune = chooseFortune()
    val book = makeBook(fortune)
    player.sendMessage("That cookie had a fortune inside it: \"${fortune}\"!")
    AllItems.give(player, book)
  }

}
