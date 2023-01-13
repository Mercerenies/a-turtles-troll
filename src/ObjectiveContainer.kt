
package com.mercerenies.turtletroll

import org.bukkit.Bukkit
import org.bukkit.scoreboard.Scoreboard
import org.bukkit.scoreboard.RenderType
import org.bukkit.scoreboard.Objective
import org.bukkit.scoreboard.Criteria

open class ObjectiveContainer(
  val name: String,
  val displayName: String,
) {

  companion object {
    private val DEFAULT_CRITERIA: Criteria =
      Criteria.create("com.mercerenies.turtletroll.ObjectiveContainer.DEFAULT_CRITERIA")
  }

  open val criteria: Criteria
    get() = DEFAULT_CRITERIA

  open fun createScoreboard(): Scoreboard =
    Bukkit.getScoreboardManager()!!.getMainScoreboard()

  val scoreboard: Scoreboard =
    createScoreboard()

  val objective: Objective =
    scoreboard.getObjective(name) ?:
      scoreboard.registerNewObjective(name, criteria, displayName, RenderType.INTEGER)

}
