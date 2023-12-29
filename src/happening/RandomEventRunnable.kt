
package com.mercerenies.turtletroll.happening

import com.mercerenies.turtletroll.feature.TurtleRunnable
import com.mercerenies.turtletroll.command.Command
import com.mercerenies.turtletroll.command.TerminalCommand
import com.mercerenies.turtletroll.Constants

import org.bukkit.command.CommandSender
import org.bukkit.plugin.Plugin

// Note: RandomEventRunnable is NOT a feature. It cannot be enabled or
// disabled. It's simply the backing engine for other features which
// fire at random intervals. The individual features (like pufferfish
// and trivia) can be turned on and off.
class RandomEventRunnable(
  plugin: Plugin,
  private val eventPool: RandomEventPool,
) : TurtleRunnable(plugin), RandomEventState {

  private inner class DebugFireCommand() : TerminalCommand() {

    override fun onCommand(sender: CommandSender): Boolean {
      run()
      return true
    }

  }

  constructor(plugin: Plugin, events: Iterable<RandomEvent>) :
    this(plugin, RandomEventPool(events))

  private var _currentTurn: Int = 0

  override val currentTurn: Int
    get() = _currentTurn

  val debugCommand: Command = DebugFireCommand()

  override val taskPeriod = Constants.IN_GAME_HOUR_TICKS.toLong()

  override fun run() {
    _currentTurn += 1
    eventPool.chooseAndFireEvent(this)
  }

}
