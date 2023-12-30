
package com.mercerenies.turtletroll.feature

import com.mercerenies.turtletroll.util.tryCancel

import org.bukkit.scheduler.BukkitRunnable
import org.bukkit.plugin.Plugin

abstract class TurtleRunnable(val plugin: Plugin) : BukkitRunnable(), Schedulable, GameModification {

  abstract val taskPeriod: Long

  open val taskDelay: Long = 1L

  override fun register() {
    this.runTaskTimer(plugin, taskDelay, taskPeriod)
  }

  override fun onPluginEnable(plugin: Plugin) {
    this.runTaskTimer(plugin, taskDelay, taskPeriod)
  }

  override fun onPluginDisable(plugin: Plugin) {
    this.tryCancel()
  }

}
