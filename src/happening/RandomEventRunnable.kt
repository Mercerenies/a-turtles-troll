
package com.mercerenies.turtletroll.happening

import com.mercerenies.turtletroll.util.component.*
import com.mercerenies.turtletroll.feature.TurtleRunnable
import com.mercerenies.turtletroll.command.Command
import com.mercerenies.turtletroll.command.OptionalUnaryCommand
import com.mercerenies.turtletroll.command.TerminalCommand
import com.mercerenies.turtletroll.Constants
import com.mercerenies.turtletroll.Messages

import org.bukkit.command.CommandSender
import org.bukkit.plugin.Plugin
import org.bukkit.Bukkit

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor

// Note: RandomEventRunnable is NOT a feature. It cannot be enabled or
// disabled. It's simply the backing engine for other features which
// fire at random intervals. The individual features (like pufferfish
// and trivia) can be turned on and off.
class RandomEventRunnable(
  plugin: Plugin,
  private val eventPool: RandomEventPool,
) : TurtleRunnable(plugin), RandomEventState {

  private inner class DebugFireCommand() : OptionalUnaryCommand() {

    override fun onCommand(sender: CommandSender, arg: String?): Boolean {
      val eventName: String
      if (arg == null) {
        // Run a random event (might be no event at all)
        _currentTurn += 1
        val event = eventPool.chooseAndFireEvent(this@RandomEventRunnable)
        eventName = event.name
      } else {
        _currentTurn += 1
        try {
          eventName = arg
          eventPool.fireEventByName(arg, this@RandomEventRunnable)
        } catch (exc: NoSuchElementException) {
          Messages.sendMessage(sender, Component.text("No event with name: $arg", NamedTextColor.RED))
          Bukkit.getLogger().warning("User $sender tried to fire an event with name $arg which does not exist")
          return false
        }
      }
      Messages.sendMessage(sender, Component.text("Fired $eventName", NamedTextColor.GREEN))
      return true
    }

    override fun onTabComplete(sender: CommandSender, arg: String?): List<String>? =
      arg?.let {
        eventPool.eventNames.filter { eventName -> eventName.startsWith(it) }
      }

  }

  private inner class DebugStatusCommand() : TerminalCommand() {

    override fun onCommand(sender: CommandSender): Boolean {
      val entriesSummary = eventPool.toJSON()
      Messages.sendMessage(sender, entriesSummary.toString())
      return true
    }

  }

  constructor(plugin: Plugin, events: Iterable<RandomEvent>) :
    this(plugin, RandomEventPool(events))

  private var _currentTurn: Int = 0

  override val currentTurn: Int
    get() = _currentTurn

  val debugFireCommand: Command = DebugFireCommand()
  val debugStatusCommand: Command = DebugStatusCommand()

  override val taskPeriod = Constants.IN_GAME_HOUR_TICKS.toLong()

  override fun run() {
    _currentTurn += 1
    eventPool.chooseAndFireEvent(this)
  }

}
