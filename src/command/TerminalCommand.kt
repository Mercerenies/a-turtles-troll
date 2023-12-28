
package com.mercerenies.turtletroll.command

import org.bukkit.command.CommandSender

abstract class TerminalCommand() : Command {

  abstract fun onCommand(
    sender: CommandSender,
  ): Boolean

  override fun onCommand(
    sender: CommandSender,
    args: List<String>,
  ): Boolean {
    if (args.size != 0) {
      return false
    }
    return onCommand(sender)
  }

  override fun onTabComplete(
    sender: CommandSender,
    args: List<String>,
  ): List<String>? =
    null

}
