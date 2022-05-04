
package com.mercerenies.turtletroll.cookie

import com.mercerenies.turtletroll.ext.*
import com.mercerenies.turtletroll.AllItems
import com.mercerenies.turtletroll.Rarity
import com.mercerenies.turtletroll.BookBuilder
import com.mercerenies.turtletroll.NameSource

import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import org.bukkit.entity.Player

class FortuneEffect(
  private val authors: NameSource,
  private val fortunes: NameSource,
) : CookieEffect {

  companion object {

    val DEFAULT_AUTHORS = listOf(
      "Albert Einstein", "Mahatma Gandhi", "Jesus Christ", "Martin Luther King, Jr.",
      "Julius Caesar", "Ulysses S. Grant", "Abraham Lincoln", "George Washington",
      "Barack Obama", "Thomas Jefferson", "Peter Capaldi", "Benedict Cumberbatch",
    )

  }

  private fun chooseAuthor(): String =
    authors.sampleName()

  private fun chooseFortune(): String =
    fortunes.sampleName()

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
