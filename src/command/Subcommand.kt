
package com.mercerenies.turtletroll.command

import com.mercerenies.turtletroll.ext.*

import org.bukkit.command.CommandSender

import kotlin.collections.Map

class Subcommand<T: Command>(
  val map: Map<String, T>,
) : Command {

  companion object {

    fun<T: Command> Single(label: String, subcommand: T): Subcommand<T> =
      Subcommand(mapOf(label to subcommand))

  }

  constructor(vararg commands: Pair<String, T>) : this(mapOf(*commands)) {}

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

    return subcommand.onCommand(sender, args.tail!!)

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
      return map.keys.filter { it.startsWith(head) }
    } else if (subcommand != null) {
      // Dispatch to the next one
      return subcommand.onTabComplete(sender, args.tail!!)
    } else {
      // Invalid subcommand
      return null
    }

  }

}
