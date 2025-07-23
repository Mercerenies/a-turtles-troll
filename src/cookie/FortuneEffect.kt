
package com.mercerenies.turtletroll.cookie

import com.mercerenies.turtletroll.Messages
import com.mercerenies.turtletroll.util.component.*
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
      "Socrates", "Napolean Bonaparte", "Winston Churchill", "Nikola Tesla",
      "Joan of Arc", "David Bowie", "Jeff Goldblum", "George Lucas", "Nelson Mandela",
      "Danny DeVito", "Nicolas Cage", "Shrek", "Kermit the Frog", "Mr. Rogers",
      "Yoda", "GLaDOS", "Spock",
    )

  }

  private fun chooseAuthor(): Component =
    authors.sampleName()

  private fun chooseFortune(): Component =
    fortunes.sampleName()

  private fun makeBook(fortune: Component): ItemStack {
    val title = Component.text("Your Fortune")
    val author = chooseAuthor()
    return BookBuilder.makeBook(title, author, listOf(fortune))
  }

  override fun cancelsDefault(): Boolean = false

  override fun onEat(action: CookieEatenAction) {
    val player = action.player
    val fortune = chooseFortune()
    val book = makeBook(fortune)
    val fortuneMessage = Component.text("That cookie had a fortune inside it: \"")
      .append(fortune)
      .append("\"!")
    Messages.sendMessage(player, fortuneMessage)
    AllItems.give(player, book)
  }

}
