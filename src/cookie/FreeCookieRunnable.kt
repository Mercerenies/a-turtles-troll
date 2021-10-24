
package com.mercerenies.turtletroll.cookie

import com.mercerenies.turtletroll.ScheduledEventRunnable
import com.mercerenies.turtletroll.AllItems

import org.bukkit.plugin.Plugin
import org.bukkit.Bukkit


class FreeCookieRunnable(plugin: Plugin) : ScheduledEventRunnable<FreeCookieRunnable.State>(plugin) {

  companion object {
    val DAWN_TIME = 0L
    val DUSK_TIME = 12000L

    val STATES = listOf(
      Event(State.Daytime, DAWN_TIME),
      Event(State.Nighttime, DUSK_TIME),
    )

  }

  enum class State {
    Daytime,
    Nighttime,
  }

  val DaytimeCookieFactory = RandomNumberCookieFactory(plugin, "Daytime Cookie")
  val NighttimeCookieFactory = RandomNumberCookieFactory(plugin, "Nighttime Cookie")

  override val name: String = "freecookie"

  override val description: String = "Everybody gets a free cookie at dawn and dusk"

  override fun getAllStates() = STATES

  override fun onStateShift(newState: State) {
    val cookieFactory = when (newState) {
      State.Daytime -> {
        DaytimeCookieFactory
      }
      State.Nighttime -> {
        NighttimeCookieFactory
      }
    }
    for (player in Bukkit.getOnlinePlayers()) {
      AllItems.give(player, cookieFactory())
    }
  }

}
