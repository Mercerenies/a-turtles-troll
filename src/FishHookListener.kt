
package com.mercerenies.turtletroll

import com.mercerenies.turtletroll.feature.AbstractFeature
import com.mercerenies.turtletroll.feature.container.FeatureContainer
import com.mercerenies.turtletroll.feature.container.ListenerContainer
import com.mercerenies.turtletroll.feature.builder.BuilderState
import com.mercerenies.turtletroll.feature.builder.FeatureContainerFactory

import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerFishEvent

class FishHookListener(
  val speed: Double = 20.0
) : AbstractFeature(), Listener {

  companion object : FeatureContainerFactory<FeatureContainer> {

    val RELEVANT_STATES = setOf(
      PlayerFishEvent.State.CAUGHT_ENTITY, PlayerFishEvent.State.IN_GROUND,
      PlayerFishEvent.State.REEL_IN,
    )

    override fun create(state: BuilderState): FeatureContainer {
      val speed = state.config.getDouble("fishhook.speed")
      return ListenerContainer(FishHookListener(speed))
    }

  }

  override val name = "fishhook"

  override val description = "Hooking an entity or ground with a fish hook has increased velocity effect"

  @EventHandler
  fun onPlayerFish(event: PlayerFishEvent) {
    if (!isEnabled()) {
      return
    }
    val state = event.getState()
    if (!RELEVANT_STATES.contains(state)) {
      return
    }
    val hook = event.getHook()
    val player = event.getPlayer()
    val entity =
      if (event.getState() == PlayerFishEvent.State.CAUGHT_ENTITY) {
        event.getCaught()!!
      } else {
        null
      }
    val delta = hook.location.clone().subtract(player.location).toVector().normalize().multiply(speed)
    player.setVelocity(delta)
    entity?.setVelocity(delta.clone().multiply(-1.0))
  }

}
