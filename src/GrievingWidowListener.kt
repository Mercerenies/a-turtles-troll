
package com.mercerenies.turtletroll

import com.mercerenies.turtletroll.feature.AbstractFeature
import com.mercerenies.turtletroll.feature.container.FeatureContainer
import com.mercerenies.turtletroll.feature.container.ListenerContainer
import com.mercerenies.turtletroll.feature.builder.BuilderState
import com.mercerenies.turtletroll.feature.builder.FeatureContainerFactory

import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.PlayerDeathEvent
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.entity.EntityType
import org.bukkit.entity.Player
import org.bukkit.scheduler.BukkitRunnable
import org.bukkit.plugin.Plugin
import org.bukkit.Location
import org.bukkit.Bukkit
import org.bukkit.GameRule

import kotlin.random.Random

class GrievingWidowListener(
  val plugin: Plugin,
  val chance: Double = 1.0,
) : AbstractFeature(), Listener {

  companion object : FeatureContainerFactory<FeatureContainer> {

    private val DEACTIVATED_MESSAGE = "mobGriefing has been deactivated."

    override fun create(state: BuilderState): FeatureContainer =
      ListenerContainer(GrievingWidowListener(state.plugin))

  }

  private inner class DeactivateMobGriefingRunnable() : BukkitRunnable() {
    override fun run() {
      setMobGriefingRule(false)
      Messages.broadcastMessage(DEACTIVATED_MESSAGE)
    }
  }

  override val name = "grievingwidow"

  override val description = "mobGriefing is off, but turns on for 10 minutes if someone dies"

  // We locally keep track of whether or not we consider mobGriefing
  // to be active. This helps us know when to show messages indicating
  // a change to the status.
  private var isMobGriefingOn: Boolean = false

  private var scheduledRunnable: DeactivateMobGriefingRunnable? = null

  private fun setMobGriefingRule(value: Boolean) {
    for (world in Bukkit.getWorlds()) {
      world.setGameRule(GameRule.MOB_GRIEFING, value)
    }
    this.isMobGriefingOn = value
  }

  private fun resetTimer() {
    try {
      scheduledRunnable?.cancel()
    } catch (_: IllegalStateException) {
      // If it wasn't scheduled or has already been canceled, don't care.
    }
    val newRunnable = DeactivateMobGriefingRunnable()
    newRunnable.runTaskLater(plugin, Constants.TICKS_PER_SECOND * 600L) // Ten minutes
    scheduledRunnable = newRunnable
  }

  override fun enable() {
    super.enable()
    setMobGriefingRule(false)
    Messages.broadcastMessage(DEACTIVATED_MESSAGE)
  }

  @EventHandler
  fun onPlayerDeath(event: PlayerDeathEvent) {
    if (!isEnabled()) {
      return
    }
    val player = event.entity
    if (!isMobGriefingOn) {
      Messages.broadcastMessage("Due to the sheer incompetence of ${player.getDisplayName()}, mobGriefing is now on.")
    }
    setMobGriefingRule(true)
    resetTimer()
  }

}
