
package com.mercerenies.turtletroll.jump

import com.mercerenies.turtletroll.Messages
import com.mercerenies.turtletroll.feature.AbstractFeature

import org.bukkit.event.EventHandler
import org.bukkit.event.Listener

import com.destroystokyo.paper.event.player.PlayerJumpEvent

import kotlin.random.Random

class EncumbranceManager(
  override val calculator: EncumbranceCalculator,
) : AbstractFeature(), Listener, EncumbranceCommand.Configuration {

  override val name = "encumbrance"

  override val description = "Jumps may fail if you're carrying too much stuff"

  @EventHandler
  fun onPlayerJump(event: PlayerJumpEvent) {
    if (!isEnabled()) {
      return
    }
    val player = event.player
    val chanceOfFailure = calculator.calculateEncumbrance(player)
    if (Random.nextDouble() < chanceOfFailure) {
      Messages.sendMessage(player, "You are over-encumbered.")
      event.setCancelled(true)
    }
  }

}
