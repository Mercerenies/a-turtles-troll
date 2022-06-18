
package com.mercerenies.turtletroll

import org.bukkit.Bukkit
import org.bukkit.scoreboard.Scoreboard
import org.bukkit.scoreboard.RenderType
import org.bukkit.scoreboard.Objective

open class ObjectiveContainer(
  val name: String,
  val displayName: String,
) {

  open val criteria: String
    get() = "dummy"

  open fun createScoreboard(): Scoreboard =
    Bukkit.getScoreboardManager()!!.getMainScoreboard()

  val scoreboard: Scoreboard =
    createScoreboard()

  val objective: Objective =
    scoreboard.getObjective(name) ?:
      scoreboard.registerNewObjective(name, criteria, displayName, RenderType.INTEGER)

}
