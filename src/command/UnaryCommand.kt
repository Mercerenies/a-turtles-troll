
package com.mercerenies.turtletroll.command

import com.mercerenies.turtletroll.ext.*

import org.bukkit.command.CommandSender


abstract class UnaryCommand() : Command {

  abstract fun onCommand(
    sender: CommandSender,
    arg: String,
  ): Boolean

  abstract fun onTabComplete(
    sender: CommandSender,
    arg: String,
  ): List<String>?

  override fun onCommand(
    sender: CommandSender,
    args: List<String>,
  ): Boolean {
    if (args.size != 1) {
      return false
    }
    return onCommand(sender, args.head!!)
  }

  override fun onTabComplete(
    sender: CommandSender,
    args: List<String>,
  ): List<String>? {
    if (args.size != 1) {
      return null
    }
    return onTabComplete(sender, args.head!!)
  }

}
