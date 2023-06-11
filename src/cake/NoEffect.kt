
package com.mercerenies.turtletroll.cake

import com.mercerenies.turtletroll.Messages

import org.bukkit.Location
import org.bukkit.entity.Player

object NoEffect : CakeEffect {

  override val positivity: Double = 0.5

  override fun cancelsDefault(): Boolean = false

  override fun onEat(loc: Location, player: Player) {
    Messages.sendMessage(player, "That cake tastes pretty good.")
  }

}
