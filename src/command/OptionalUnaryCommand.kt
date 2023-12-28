
package com.mercerenies.turtletroll.command

import com.mercerenies.turtletroll.util.head

import org.bukkit.command.CommandSender

abstract class OptionalUnaryCommand() : Command {

  abstract fun onCommand(
    sender: CommandSender,
    arg: String?,
  ): Boolean

  abstract fun onTabComplete(
    sender: CommandSender,
    arg: String?,
  ): List<String>?

  override fun onCommand(
    sender: CommandSender,
    args: List<String>,
  ): Boolean =
    when (args.size) {
      0 -> onCommand(sender, null)
      1 -> onCommand(sender, args.head!!)
      else -> false
    }

  override fun onTabComplete(
    sender: CommandSender,
    args: List<String>,
  ): List<String>? =
    when (args.size) {
      0 -> onTabComplete(sender, null)
      1 -> onTabComplete(sender, args.head!!)
      else -> null
    }

}
