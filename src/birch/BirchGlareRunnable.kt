
package com.mercerenies.turtletroll.birch

import com.mercerenies.turtletroll.util.component.*
import com.mercerenies.turtletroll.feature.RunnableFeature
import com.mercerenies.turtletroll.location.BlockSelector
import com.mercerenies.turtletroll.gravestone.CustomDeathMessageRegistry
import com.mercerenies.turtletroll.gravestone.CustomDeathMessage
import com.mercerenies.turtletroll.gravestone.Birch

import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.plugin.Plugin

import net.kyori.adventure.text.Component

class BirchGlareRunnable(
  plugin: Plugin,
  private val deathRegistry: CustomDeathMessageRegistry,
) : RunnableFeature(plugin) {

  companion object {

    private val BIRCH_RADIUS = 5
    private val MAX_SIGHT_DISTANCE = 16

    private fun isNearBirch(player: Player): Boolean =
      BlockSelector.getNearby(player.location, BIRCH_RADIUS).any { it.block.type == Material.BIRCH_LOG }

    private fun isLookingAtBirch(player: Player): Boolean =
      player.getTargetBlockExact(MAX_SIGHT_DISTANCE)?.type == Material.BIRCH_LOG

  }

  override val name: String = "birchglare"

  override val description: String = "If you get near a birch tree and are not staring at it, it will attack"

  override val taskPeriod = 9L

  override fun run() {
    if (!isEnabled()) {
      return
    }
    val onlinePlayers = Bukkit.getOnlinePlayers().toSet()
    for (player in onlinePlayers) {
      if (isNearBirch(player) && !isLookingAtBirch(player)) {
        val customMessage = CustomDeathMessage(
          Birch,
          Component.text("").append(player.displayName()).append(" was attacked by a tree."),
        )
        deathRegistry.withCustomDeathMessage(customMessage) {
          player.damage(2.0, null)
        }
      }
    }
  }

}
