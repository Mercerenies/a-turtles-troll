
package com.mercerenies.turtletroll.cookie

import com.mercerenies.turtletroll.Messages
import com.mercerenies.turtletroll.ext.*
import com.mercerenies.turtletroll.AllItems
import com.mercerenies.turtletroll.BookBuilder
import com.mercerenies.turtletroll.NameSource

import org.bukkit.inventory.ItemStack

import net.kyori.adventure.text.Component

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

  private fun makeBook(fortune: Component): ItemStack {
    val title = Component.text("Your Fortune")
    val author = Component.text(chooseAuthor())
    return BookBuilder.makeBook(title, author, listOf(fortune))
  }

  override fun cancelsDefault(): Boolean = false

  override fun onEat(action: CookieEatenAction) {
    val player = action.player
    val fortune = chooseFortune()
    val book = makeBook(Component.text(fortune))
    Messages.sendMessage(player, "That cookie had a fortune inside it: \"${fortune}\"!")
    AllItems.give(player, book)
  }

}
