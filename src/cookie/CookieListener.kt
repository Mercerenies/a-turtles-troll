
package com.mercerenies.turtletroll.cookie

import com.mercerenies.turtletroll.feature.AbstractFeature
import com.mercerenies.turtletroll.feature.container.FeatureContainer
import com.mercerenies.turtletroll.feature.container.ListenerContainer
import com.mercerenies.turtletroll.feature.builder.BuilderState
import com.mercerenies.turtletroll.feature.builder.FeatureContainerFactory
import com.mercerenies.turtletroll.SpawnReason
import com.mercerenies.turtletroll.ext.*
import com.mercerenies.turtletroll.Weight
import com.mercerenies.turtletroll.sample
import com.mercerenies.turtletroll.Constants
import com.mercerenies.turtletroll.mimic.MimicListener
import com.mercerenies.turtletroll.location.BlockSelector
import com.mercerenies.turtletroll.gravestone.CustomDeathMessageRegistry
import com.mercerenies.turtletroll.gravestone.Cookie

import org.bukkit.entity.Player
import org.bukkit.entity.EntityType
import org.bukkit.plugin.Plugin
import org.bukkit.Material
import org.bukkit.event.Listener
import org.bukkit.event.EventHandler
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerItemConsumeEvent
import org.bukkit.event.entity.CreatureSpawnEvent
import org.bukkit.scheduler.BukkitRunnable
import org.bukkit.block.`data`.Lightable

import kotlin.collections.HashMap
import kotlin.random.Random
import kotlin.math.pow

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
      val action = CookieEatenAction(event.item, event.player)
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
