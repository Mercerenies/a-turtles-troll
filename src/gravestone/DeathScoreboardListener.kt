
package com.mercerenies.turtletroll.gravestone

import com.mercerenies.turtletroll.feature.AbstractFeature
import com.mercerenies.turtletroll.storage.GlobalDataHolder
import com.mercerenies.turtletroll.Constants
import com.mercerenies.turtletroll.ext.*

import org.bukkit.Bukkit
import org.bukkit.World
import org.bukkit.event.Listener
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.server.PluginEnableEvent
import org.bukkit.event.server.PluginDisableEvent
import org.bukkit.event.server.ServerLoadEvent
import org.bukkit.event.entity.PlayerDeathEvent
import org.bukkit.scoreboard.ScoreboardManager
import org.bukkit.scoreboard.Scoreboard
import org.bukkit.scoreboard.RenderType
import org.bukkit.scoreboard.DisplaySlot
import org.bukkit.scoreboard.Objective
import org.bukkit.scheduler.BukkitRunnable
import org.bukkit.plugin.Plugin
import org.bukkit.block.Block

import org.json.JSONObject

class DeathScoreboardListener(
  val plugin: Plugin,
  val pluginData: GlobalDataHolder,
) : AbstractFeature(), Listener {

  companion object {
    private val SCOREBOARD_NAME = "com.mercerenies.turtletroll.gravestone.DeathScoreboardFeature.SCOREBOARD_NAME"
    private val DATA_KEY = "com.mercerenies.turtletroll.gravestone.DeathScoreboardFeature.DATA_KEY"
  }

  // I'm putting scoreboard and deathObjective in here to be
  // initialized later (specifically, when a ServerLoadEvent is
  // fired), so that we only have one potentially-null value floating
  // around rather than several. There's only one late-initialization
  // happening.
  private inner class ObjectiveContainer() {

    val scoreboard =
      Bukkit.getScoreboardManager()!!.getNewScoreboard()

    val deathObjective =
      scoreboard.registerNewObjective(SCOREBOARD_NAME, "deathCount", "Deaths", RenderType.INTEGER)

  }

  override val name = "deathscoreboard"

  override val description = "A scoreboard shows how many times each player has died"

  private var container: ObjectiveContainer? = null

  private fun loadData() {
    val container = ObjectiveContainer()
    this.container = container
    val objective = container.deathObjective
    objective.displaySlot = DisplaySlot.SIDEBAR
    val loadedData = try {
      JSONObject(pluginData.getData(DATA_KEY) ?: "{}")
    } catch (exc: Exception) {
      JSONObject("{}")
    }
    for (key in loadedData.keys()) {
      val value = loadedData.get(key)
      val score = objective.getScore(key)
      if (value is Number) {
        score.setScore(value.toInt())
      } else {
        score.setScore(0)
      }
    }
  }

  override fun enable() {
    super.enable()
    container?.deathObjective?.displaySlot = DisplaySlot.SIDEBAR
  }

  override fun disable() {
    super.disable()
    container?.deathObjective?.displaySlot = null
  }

  private fun getScoreboardAsJSON(): JSONObject {
    val jsonObject = JSONObject("{}")
    val container = this.container
    if (container != null) {
      val scoreboard = container.scoreboard
      for (key in scoreboard.getEntries()) {
        val value = container.deathObjective.getScore(key).getScore()
        jsonObject.put(key, value)
      }
    }
    return jsonObject
  }

  @EventHandler
  @Suppress("UNUSED_PARAMETER")
  fun onPlayerDeath(_event: PlayerDeathEvent) {
    if (!isEnabled()) {
      return
    }

    // Update file data
    pluginData.putData(DATA_KEY, getScoreboardAsJSON().toString())

  }

  @EventHandler
  @Suppress("UNUSED_PARAMETER")
  fun onServerLoad(_event: ServerLoadEvent) {
    loadData()
  }

}
