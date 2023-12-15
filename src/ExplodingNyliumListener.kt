
package com.mercerenies.turtletroll

import com.mercerenies.turtletroll.feature.HasEnabledStatus
import com.mercerenies.turtletroll.feature.AbstractFeature

import org.bukkit.Material
import org.bukkit.entity.TNTPrimed
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerMoveEvent
import org.bukkit.FluidCollisionMode

class ExplodingNyliumListener(
  val pumpkinFeature: HasEnabledStatus,
) : AbstractFeature(), Listener {

  companion object {

    private val MAX_SIGHT_DISTANCE: Int = 16

    private fun isNylium(material: Material): Boolean =
      (material == Material.CRIMSON_NYLIUM) || (material == Material.WARPED_NYLIUM)

  }

  override val name = "nylium"

  override val description = "Nylium turns into primed TNT when you look at it"

  @EventHandler
  fun onPlayerMove(event: PlayerMoveEvent) {
    if (!isEnabled()) {
      return
    }
    if ((Hats.isWearingOrdinaryHat(event.player)) && (pumpkinFeature.isEnabled())) {
      return
    }

    val targetBlock = event.player.getTargetBlockExact(MAX_SIGHT_DISTANCE, FluidCollisionMode.ALWAYS)
    if ((targetBlock != null) && (isNylium(targetBlock.type))) {
      targetBlock.type = Material.AIR
      val tnt = targetBlock.world.spawn(targetBlock.location.add(0.5, 0.5, 0.5), TNTPrimed::class.java)
      tnt.setFuseTicks((Constants.TICKS_PER_SECOND * 4).toInt())
    }
  }

}
