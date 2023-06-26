
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

class LevitationListener(
  private val effectSeconds: Int,
) : AbstractFeature(), Listener {

  companion object : FeatureContainerFactory<FeatureContainer> {

    override fun create(state: BuilderState): FeatureContainer =
      ListenerContainer(LevitationListener(state.config.getInt("levitation.levitation_seconds")))

  }

  override val name = "levitation"

  override val description = "Certain blocks cause the player to levitate"

  @EventHandler
  fun onPlayerMove(event: PlayerMoveEvent) {
    if (!isEnabled()) {
      return
    }
    val loc = event.getTo()
    val block = loc.clone().add(0.0, -1.0, 0.0).getBlock()
    if (BlockTypes.ORES.contains(block.type)) {
      val player = event.player
      player.addPotionEffect(PotionEffect(PotionEffectType.LEVITATION, Constants.TICKS_PER_SECOND * effectSeconds, 0))
    }
  }

}
