
package com.mercerenies.turtletroll.command

import com.mercerenies.turtletroll.util.head
import com.mercerenies.turtletroll.util.tail

import org.bukkit.command.CommandSender

import kotlin.collections.Map

class Subcommand<T : Command>(
  val map: Map<String, PermittedCommand<T>>,
) : Command {

  companion object {

    fun<T : Command> Single(label: String, subcommand: PermittedCommand<T>): Subcommand<T> =
      Subcommand(mapOf(label to subcommand))

    fun<T : Command> Single(label: String, subcommand: T, permission: String): Subcommand<T> =
      Single(label, PermittedCommand(subcommand, permission))

  }

  constructor(vararg commands: Pair<String, PermittedCommand<T>>) : this(mapOf(*commands)) {}

  override fun onCommand(
    sender: CommandSender,
    args: List<String>,
  ): Boolean {
    val head = args.head
    if (head == null) {
      return false
    }
    val subcommand = map[head]
    if (subcommand == null) {
      return false
    }

    if (sender.hasPermission(subcommand.permission)) {
      return subcommand.command.onCommand(sender, args.tail!!)
    } else {
      return false
    }

  }

  override fun onTabComplete(
    sender: CommandSender,
    args: List<String>,
  ): List<String>? {
    val head = args.head
    if (head == null) {
      return null
    }

    val subcommand = map[head]
    if (args.size == 1) {
      // We're still typing the head, so autocomplete right here
      return map.keys.filter { sender.hasPermission(map[it]!!.permission) && it.startsWith(head) }
    } else if ((subcommand != null) && (sender.hasPermission(subcommand.permission))) {
      // Dispatch to the next one
      return subcommand.command.onTabComplete(sender, args.tail!!)
    } else {
      // Invalid subcommand
      return null
    }

  }

}
