
package com.mercerenies.turtletroll

import com.mercerenies.turtletroll.feature.AbstractFeature
import com.mercerenies.turtletroll.feature.container.FeatureContainer
import com.mercerenies.turtletroll.feature.container.ListenerContainer
import com.mercerenies.turtletroll.feature.builder.BuilderState
import com.mercerenies.turtletroll.feature.builder.FeatureContainerFactory

import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerMoveEvent
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType

class SlowSlabListener(
  private val slowTimeSeconds: Int,
  private val slownessLevel: Int,
) : AbstractFeature(), Listener {

  companion object : FeatureContainerFactory<FeatureContainer> {
    val BLOCKS = BlockTypes.SLABS union BlockTypes.STAIRS

    override fun create(state: BuilderState): FeatureContainer =
      ListenerContainer(
        SlowSlabListener(
          slowTimeSeconds = state.config.getInt("slowslab.slow_time_seconds"),
          slownessLevel = state.config.getInt("slowslab.slowness_level") - 1,
        )
      )

  }

  private val bootsDamager = BootsDamager()

  override val name = "slowslab"

  override val description = "Half slabs and stairs slow you down"

  @EventHandler
  fun onPlayerMove(event: PlayerMoveEvent) {
    if (!isEnabled()) {
      return
    }
    val block = event.getTo().getBlock()
    if (BLOCKS.contains(block.type)) {
      val player = event.player
      if (!bootsDamager.tryWearDownBoots(player)) {
        player.addPotionEffect(PotionEffect(PotionEffectType.SLOW, Constants.TICKS_PER_SECOND * slowTimeSeconds, slownessLevel))
      }
    }
  }

}
