
package com.mercerenies.turtletroll.prisoner

import com.mercerenies.turtletroll.command.TerminalCommand
import com.mercerenies.turtletroll.Messages

import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class DilemmaResponseCommand(
  val response: PlayerResponse,
  private val currentDilemmaSupplier: () -> PrisonersDilemma?,
) : TerminalCommand() {

  companion object {
    val PLAYER_ONLY_MESSAGE = "Only players can respond to prisoner's dilemmas"
    val NO_DILEMMA_MESSAGE = "There is no active prisoner's dilemma right now"
    val ACCEPTED_MESSAGE = "Dilemma response accepted!"
    val NOT_INVOLVED_MESSAGE = "You are not a participant in this prisoner's dilemma"
  }

  override fun onCommand(sender: CommandSender): Boolean {
    if (sender !is Player) {
      Messages.sendMessage(sender, PLAYER_ONLY_MESSAGE)
      return true
    }
    val currentDilemma = currentDilemmaSupplier()
    if (currentDilemma == null) {
      Messages.sendMessage(sender, NO_DILEMMA_MESSAGE)
      return true
    }
    val result = currentDilemma.acceptResponse(sender, response)
    if (result) {
      Messages.sendMessage(sender, ACCEPTED_MESSAGE)
    } else {
      Messages.sendMessage(sender, NOT_INVOLVED_MESSAGE)
    }
    return true
  }

}
