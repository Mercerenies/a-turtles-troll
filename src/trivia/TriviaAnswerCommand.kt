
package com.mercerenies.turtletroll.trivia

import com.mercerenies.turtletroll.feature.HasEnabledStatus
import com.mercerenies.turtletroll.command.UnaryCommand
import com.mercerenies.turtletroll.Messages

import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class TriviaAnswerCommand(
  private val feature: HasEnabledStatus,
  private val engine: TriviaEngine,
) : UnaryCommand() {

  override fun onCommand(
    sender: CommandSender,
    arg: String,
  ): Boolean {
    if (!feature.isEnabled()) {
      Messages.sendMessage(sender, "That feature is currently disabled.")
      return false
    }
    if (sender !is Player) {
      Messages.sendMessage(sender, "That command can only be used by players.")
      return false
    }
    val acceptance = engine.acceptAnswer(sender, arg)
    if (acceptance.isSuccessful()) {
      Messages.sendMessage(sender, "Answer accepted!")
    } else {
      Messages.sendMessage(sender, acceptance.errorMessage)
    }
    return true
  }

  // TODO Tab completion for these :)
  override fun onTabComplete(
    sender: CommandSender,
    arg: String,
  ): List<String>? =
    null

}
