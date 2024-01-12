
package com.mercerenies.turtletroll.temperature

import com.mercerenies.turtletroll.feature.HasEnabledStatus
import com.mercerenies.turtletroll.util.component.*
import com.mercerenies.turtletroll.command.TerminalCommand
import com.mercerenies.turtletroll.Messages

import org.bukkit.entity.Player
import org.bukkit.command.CommandSender

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor

class TemperatureCommand(
  private val feature: HasEnabledStatus,
  private val calculator: TemperatureCalculator,
) : TerminalCommand() {

  companion object {
    val DISABLED_MESSAGE = "Temperature is currently disabled on this server"
    val PLAYER_ONLY_MESSAGE = "This command can only be used by players"
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
    explainToPlayer(sender)
    return true
  }

  private fun explainToPlayer(player: Player) {
    explainCurrentTemperature(player)
    explainSafetyFor(player, "cold", calculator.coldConditions)
    explainSafetyFor(player, "hot", calculator.hotConditions)
  }

  private fun explainCurrentTemperature(player: Player) {
    val temperature = TemperatureCalculator.getTemperature(player)
    if (temperature < calculator.minSafeTemperature) {
      Messages.sendMessage(
        player,
        Component.text("You are currently in ").append(Component.text("COLD WEATHER", NamedTextColor.AQUA))
      )
    } else if (temperature > calculator.maxSafeTemperature) {
      Messages.sendMessage(
        player,
        Component.text("You are currently in ").append(Component.text("HOT WEATHER", NamedTextColor.DARK_RED))
      )
    } else {
      Messages.sendMessage(player, "You are currently in a safe temperature")
    }
  }

  private fun explainSafetyFor(player: Player, weatherType: String, conditions: List<BiomeSafetyCondition>) {
    val evaluatedConditions = conditions.map { it.evaluate(player) }
    val safeCondition = evaluatedConditions.find { it.isSafe() }
    if (safeCondition != null) {
      Messages.sendMessage(player, "You are currently SAFE in ${weatherType} weather because: ${safeCondition.explanation}")
    } else {
      Messages.sendMessage(player, "You are currently NOT SAFE in ${weatherType} weather because:")
      for (condition in evaluatedConditions) {
        Messages.sendMessage(player, "* ${condition.explanation}")
      }
    }
  }

}
