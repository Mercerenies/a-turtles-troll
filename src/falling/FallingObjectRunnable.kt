
package com.mercerenies.turtletroll.falling

import com.mercerenies.turtletroll.feature.RunnableFeature

import org.bukkit.entity.Player
import org.bukkit.plugin.Plugin
import org.bukkit.Bukkit

abstract class FallingObjectRunnable(plugin: Plugin) : RunnableFeature(plugin) {

  abstract val blockDropper: BlockDropper

  open fun shouldDropOn(player: Player): Boolean = true

  open fun updatePlayer(player: Player) {}

  override fun run() {
    if (!isEnabled()) {
      return
    }
    val players = Bukkit.getServer().getOnlinePlayers()
    for (player in players) {
      if (shouldDropOn(player)) {
        blockDropper.doDrop(player)
      }
      updatePlayer(player)
    }
  }

}
