
package com.mercerenies.turtletroll.falling

import com.mercerenies.turtletroll.feature.Feature

import org.bukkit.scheduler.BukkitRunnable
import org.bukkit.entity.Player
import org.bukkit.plugin.Plugin
import org.bukkit.Location
import org.bukkit.Bukkit
import org.bukkit.Material

abstract class FallingObjectRunnable : BukkitRunnable(), Feature {
  private var _enabled: Boolean = true

  companion object {
    val TICKS_PER_SECOND = 20
  }

  abstract val maxDropHeight: Int

  abstract val blockToDrop: Material

  abstract val delayTime: Long

  override fun enable() {
    _enabled = true
  }

  override fun disable() {
    _enabled = false
  }

  override fun isEnabled() = _enabled

  open fun shouldDropOn(player: Player): Boolean = true

  open fun updatePlayer(player: Player) {}

  fun doDrop(player: Player) {
    val loc = player.location
    loc.y += 1
    var maxDistLeft = maxDropHeight
    while ((maxDistLeft > 0) && (loc.getBlock().isEmpty())) {
      maxDistLeft -= 1
      loc.y += 1
    }
    loc.y -= 1
    loc.getBlock().type = blockToDrop
  }

  override fun run() {
    if (!_enabled) {
      return
    }
    val players = Bukkit.getServer().getOnlinePlayers()
    for (player in players) {
      if (shouldDropOn(player)) {
        doDrop(player)
      }
      updatePlayer(player)
    }
  }

  fun register(plugin: Plugin) {
    this.runTaskTimer(plugin, 1L, delayTime)
  }

}
