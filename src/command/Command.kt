
package com.mercerenies.turtletroll.command

import org.bukkit.command.CommandSender

// A simplified interface similar to that of CommandExecutor and
// TabCompleter combined. Intended to be composed.
interface Command {

  fun onCommand(
    sender: CommandSender,
    args: List<String>,
  ): Boolean

  fun onTabComplete(
    sender: CommandSender,
    args: List<String>,
  ): List<String>?

}
