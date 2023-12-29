
package com.mercerenies.turtletroll.feature

import org.bukkit.scheduler.BukkitRunnable
import org.bukkit.plugin.Plugin

abstract class TurtleRunnable(val plugin: Plugin) : BukkitRunnable(), Schedulable {

  abstract val taskPeriod: Long

  open val taskDelay: Long = 1L

  override fun register() {
    this.runTaskTimer(plugin, taskDelay, taskPeriod)
  }

}
