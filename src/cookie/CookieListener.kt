
package com.mercerenies.turtletroll.cookie

import com.mercerenies.turtletroll.feature.AbstractFeature
import com.mercerenies.turtletroll.util.component.*
import com.mercerenies.turtletroll.Weight
import com.mercerenies.turtletroll.sample
import com.mercerenies.turtletroll.gravestone.CustomDeathMessageRegistry

import org.bukkit.plugin.Plugin
import org.bukkit.Material
import org.bukkit.event.Listener
import org.bukkit.event.EventHandler
import org.bukkit.event.player.PlayerItemConsumeEvent

class CookieListener(
  val plugin: Plugin,
  val effects: List<Weight<CookieEffect>>,
  private val deathRegistry: CustomDeathMessageRegistry,
) : AbstractFeature(), Listener {

  companion object {

    fun isCookie(material: Material): Boolean =
      material == Material.COOKIE

  }

  override val name = "cookies"

  override val description = "Eating a cookie has a random effect"

  @EventHandler
  fun onPlayerItemConsume(event: PlayerItemConsumeEvent) {
    if (!isEnabled()) {
      return
    }

    val material = event.item.type
    if (isCookie(material)) {
      val effect = chooseEffect()
      val action = CookieEatenAction(event.item, event.player, deathRegistry)
      effect.onEat(action)
      if (effect.cancelsDefault()) {
        event.setCancelled(true)
      }
    }
  }

  private fun chooseEffect(): CookieEffect {
    val weights = effects
    return sample(weights)
  }

}
