
package com.mercerenies.turtletroll.falling

import com.mercerenies.turtletroll.feature.RunnableFeature

import org.bukkit.entity.Player
import org.bukkit.plugin.Plugin
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.block.Block

abstract class FallingObjectRunnable(plugin: Plugin) : RunnableFeature(plugin) {

  open val minDropHeight: Int = 1

  abstract val maxDropHeight: Int

  abstract fun getBlockToDrop(): Material

  open fun shouldDropOn(player: Player): Boolean = true

  open fun updatePlayer(player: Player) {}

  open fun canDropThroughBlock(block: Block): Boolean =
    block.isEmpty()

  fun doDrop(player: Player) {
    val loc = player.location
    loc.y += minDropHeight
    var maxDistLeft = (maxDropHeight - minDropHeight)
    while ((maxDistLeft > 0) && (canDropThroughBlock(loc.getBlock()))) {
      maxDistLeft -= 1
      loc.y += 1
    }
    loc.y -= 1
    if (loc.getBlock().type == Material.AIR) {
      loc.getBlock().type = getBlockToDrop()
    }
  }

  override fun run() {
    if (!isEnabled()) {
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

}
