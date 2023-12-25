
package com.mercerenies.turtletroll.feature.container

import com.mercerenies.turtletroll.Messages
import com.mercerenies.turtletroll.command.Command
import com.mercerenies.turtletroll.command.TerminalCommand

import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

// Feature container which defines a single debug command on
// top of whatever is provided by the underlying container.
class DebugCommandFeatureContainer(
  private val underlying: FeatureContainer,
  private val command: Pair<String, Command>,
) : FeatureContainer by underlying {

  companion object {
    val NO_CONSOLE_MESSAGE = "Only players can use this command"
  }

  override val debugCommands: Iterable<Pair<String, Command>>
    get() = underlying.debugCommands + command

}

fun FeatureContainer.withDebugCommand(command: Pair<String, Command>): DebugCommandFeatureContainer =
  DebugCommandFeatureContainer(this, command)

fun FeatureContainer.withDebugCommand(commandName: String, command: Command): DebugCommandFeatureContainer =
  withDebugCommand(commandName to command)

fun FeatureContainer.withDebugCommand(commandName: String, callback: (CommandSender) -> Boolean): DebugCommandFeatureContainer {
  val command = object : TerminalCommand() {
    override fun onCommand(sender: CommandSender) =
      callback(sender)
  }
  return withDebugCommand(commandName, command)
}

@JvmName("withDebugCommandPlayer")
fun FeatureContainer.withDebugCommand(commandName: String, callback: (Player) -> Boolean): DebugCommandFeatureContainer =
  withDebugCommand(commandName) { sender: CommandSender ->
    if (sender is Player) {
      callback(sender)
    } else {
      Messages.sendMessage(sender, DebugCommandFeatureContainer.NO_CONSOLE_MESSAGE)
      true
    }
  }

@JvmName("withDebugCommandNullary")
fun FeatureContainer.withDebugCommand(commandName: String, callback: () -> Boolean): DebugCommandFeatureContainer =
  withDebugCommand(commandName) { _: CommandSender -> callback() }
