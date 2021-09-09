
package com.mercerenies.turtletroll

import com.mercerenies.turtletroll.feature.AbstractFeature

import org.bukkit.Material
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerFishEvent
import org.bukkit.entity.Player
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import org.bukkit.inventory.meta.Damageable

import kotlin.random.Random

// This is really janky, even by my standards, so it's not currently
// used :)

class FishHookListener(
  val speed: Double = 20.0
): AbstractFeature(), Listener {

  companion object {
    val RELEVANT_STATES = setOf(
      PlayerFishEvent.State.CAUGHT_ENTITY, PlayerFishEvent.State.IN_GROUND,
    )
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
    val delta = hook.location.subtract(player.location).getDirection().multiply(speed)
    player.setVelocity(delta)
    entity?.setVelocity(delta.clone().multiply(-1.0))
  }

}
