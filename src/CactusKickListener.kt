
package com.mercerenies.turtletroll

import com.mercerenies.turtletroll.util.component.*
import com.mercerenies.turtletroll.feature.AbstractFeature
import com.mercerenies.turtletroll.feature.HasEnabledStatus

import org.bukkit.Material
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerMoveEvent
import org.bukkit.FluidCollisionMode

import net.kyori.adventure.text.Component

class CactusKickListener(
  private val pumpkinFeature: HasEnabledStatus,
) : AbstractFeature(), Listener {

  companion object {
    private val MAX_SIGHT_DISTANCE: Int = 16
  }

  override val name = "cactuskick"

  override val description = "Looking at a cactus kicks the player"

  @EventHandler
  fun onPlayerMove(event: PlayerMoveEvent) {
    if (!isEnabled()) {
      return
    }
    val player = event.player

    // If the pumpkin effect is enabled and the player is wearing a
    // pumpkin, they can look at cacti.
    if ((Hats.isWearingOrdinaryHat(player)) && (pumpkinFeature.isEnabled())) {
      return
    }

    val targetBlock = event.player.getTargetBlockExact(MAX_SIGHT_DISTANCE, FluidCollisionMode.ALWAYS)
    if ((targetBlock != null) && (targetBlock.type == Material.CACTUS)) {
      targetBlock.breakNaturally(true)
      player.kick(Component.text("Ogled a cactus"))
    }
  }

}
