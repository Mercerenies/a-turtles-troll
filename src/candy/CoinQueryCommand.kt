
package com.mercerenies.turtletroll.candy

import com.mercerenies.turtletroll.feature.HasEnabledStatus
import com.mercerenies.turtletroll.command.TerminalCommand
import com.mercerenies.turtletroll.Messages

import org.bukkit.entity.Player
import org.bukkit.command.CommandSender

class CoinQueryCommand(
  private val coinStore: CoinStore,
  private val feature: HasEnabledStatus,
) : TerminalCommand() {
  companion object {
    val DISABLED_MESSAGE = "Candy Shop is currently disabled on this server"
    val PLAYER_ONLY_MESSAGE = "This command cannot be used from the server console"

    fun resultMessage(coins: Int): String =
      if (coins == 1) {
        "You have 1 coin"
      } else {
        "You have $coins coins"
      }
  }

  fun isEnabled(): Boolean =
    feature.isEnabled()

  override fun onCommand(sender: CommandSender): Boolean {
    if (!isEnabled()) {
      Messages.sendMessage(sender, DISABLED_MESSAGE)
      return true
    }
    if (sender !is Player) {
      Messages.sendMessage(sender, PLAYER_ONLY_MESSAGE)
      return true
    }

    val coins = coinStore.getCoins(sender)
    Messages.sendMessage(sender, resultMessage(coins))
    return true
  }

}
