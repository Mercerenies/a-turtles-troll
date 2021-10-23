
package com.mercerenies.turtletroll.command

import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.command.Command as BCommand
import org.bukkit.command.TabCompleter

class CommandDispatcher<T : Command>(
  val toplevel: T,
) : CommandExecutor, TabCompleter {

  override fun onCommand(
    sender: CommandSender,
    command: BCommand,
    label: String,
    args: Array<String>,
  ): Boolean {
    val fullArgs = listOf(label) + args.toList()
    return toplevel.onCommand(sender, fullArgs)
  }

  override fun onTabComplete(
    sender: CommandSender,
    command: BCommand,
    alias: String,
    args: Array<String>,
  ): List<String>? {
    val fullArgs = listOf(alias) + args.toList()
    return toplevel.onTabComplete(sender, fullArgs)
  }

}
