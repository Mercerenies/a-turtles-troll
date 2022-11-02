
package com.mercerenies.turtletroll

import com.mercerenies.turtletroll.feature.RunnableFeature

import org.bukkit.plugin.Plugin
import org.bukkit.Bukkit
import org.bukkit.World

abstract class ScheduledEventRunnable<S>(plugin: Plugin) : RunnableFeature(plugin) {

  companion object {

    fun getSystemTime(): Long {
      for (world in Bukkit.getServer().getWorlds()) {
        if (world.environment == World.Environment.NORMAL) {
          return world.getTime()
        }
      }
      return 0L // That's not good :(
    }

    fun getAbsoluteSystemTime(): Long {
      for (world in Bukkit.getServer().getWorlds()) {
        if (world.environment == World.Environment.NORMAL) {
          return world.getFullTime()
        }
      }
      return 0L // That's not good :(
    }

  }

  data class Event<S>(
    val state: S,
    val timestamp: Long,
  )

  open override val taskPeriod = Constants.TICKS_PER_SECOND * 5L

  open override val taskDelay = Constants.TICKS_PER_SECOND * 5L

  var currentState: S = getDefaultState()
    private set

  open fun getDefaultState(): S =
    getAllStates()[0].state

  // Note: The first state *must* start at timestamp 0. Each pair
  // indicates a state and the time at which that state *starts*. This
  // list should be in strictly ascending order by the timestamp.
  abstract fun getAllStates(): List<Event<S>>

  // This is called immediately *before* the state shifts. Thus,
  // this.currentState is still the previous state.
  abstract fun onStateShift(newState: S)

  private fun getStateBasedOnTime(time: Long): S =
    getAllStates().filter { it.timestamp <= time }.last().state

  open override fun enable() {
    super.enable()
    currentState = getDefaultState()
  }

  open override fun run() {
    if (!isEnabled()) {
      return
    }

    val time = getSystemTime()
    val newState = getStateBasedOnTime(time)
    if (newState != currentState) {
      onStateShift(newState)
      currentState = newState
    }

  }

}
