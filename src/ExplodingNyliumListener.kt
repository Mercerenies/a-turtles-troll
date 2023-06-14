
package com.mercerenies.turtletroll

import com.mercerenies.turtletroll.feature.AbstractFeature
import com.mercerenies.turtletroll.feature.container.FeatureContainer
import com.mercerenies.turtletroll.feature.container.ListenerContainer
import com.mercerenies.turtletroll.feature.builder.BuilderState
import com.mercerenies.turtletroll.feature.builder.FeatureContainerFactory

import org.bukkit.Material
import org.bukkit.entity.TNTPrimed
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerMoveEvent
import org.bukkit.FluidCollisionMode
import org.bukkit.Location
import org.bukkit.scheduler.BukkitRunnable
import org.bukkit.plugin.Plugin

import kotlin.collections.HashSet

class ExplodingNyliumListener() : AbstractFeature(), Listener {

  companion object : FeatureContainerFactory<FeatureContainer> {

    private val MAX_SIGHT_DISTANCE: Int = 16

    private fun isNylium(material: Material): Boolean =
      (material == Material.CRIMSON_NYLIUM) || (material == Material.WARPED_NYLIUM)

    override fun create(state: BuilderState): FeatureContainer =
      ListenerContainer(ExplodingNyliumListener())

  }

  override val name = "nylium"

  override val description = "Nylium turns into primed TNT when you look at it"

  @EventHandler
  fun onPlayerMove(event: PlayerMoveEvent) {
    if (!isEnabled()) {
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
