
package com.mercerenies.turtletroll.jump

import com.mercerenies.turtletroll.feature.HasEnabledStatus
import com.mercerenies.turtletroll.command.TerminalCommand
import com.mercerenies.turtletroll.Messages

import org.bukkit.entity.Player
import org.bukkit.command.CommandSender

class EncumbranceCommand(
  private val config: Configuration,
) : TerminalCommand() {

  companion object {
    val DISABLED_MESSAGE = "Encumbrance is currently disabled on this server"
    val PLAYER_ONLY_MESSAGE = "This command can only be used by players"
  }

  interface Configuration : HasEnabledStatus {

    val calculator: EncumbranceCalculator

  }

  fun isEnabled(): Boolean =
    config.isEnabled()

  override fun onCommand(sender: CommandSender): Boolean {
    if (!isEnabled()) {
      Messages.sendMessage(sender, DISABLED_MESSAGE)
      return true
    }
    if (sender !is Player) {
      Messages.sendMessage(sender, PLAYER_ONLY_MESSAGE)
      return true
    }
    config.calculator.explain(sender)
    return true
  }

}
