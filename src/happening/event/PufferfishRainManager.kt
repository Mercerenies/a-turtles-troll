
package com.mercerenies.turtletroll.happening.event

import com.mercerenies.turtletroll.feature.AbstractFeature
import com.mercerenies.turtletroll.feature.container.FeatureContainer
import com.mercerenies.turtletroll.feature.builder.FeatureBuilder
import com.mercerenies.turtletroll.feature.builder.BuilderState
import com.mercerenies.turtletroll.feature.builder.FeatureContainerFactory
import com.mercerenies.turtletroll.happening.RandomEvent
import com.mercerenies.turtletroll.happening.NotifiedRandomEvent
import com.mercerenies.turtletroll.happening.RandomEventState
import com.mercerenies.turtletroll.happening.withTitle
import com.mercerenies.turtletroll.happening.withCooldown
import com.mercerenies.turtletroll.happening.boundToFeature

import org.bukkit.entity.Entity
import org.bukkit.entity.Player
import org.bukkit.entity.PufferFish
import org.bukkit.plugin.Plugin
import org.bukkit.Bukkit
import org.bukkit.event.EventHandler
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.event.entity.EntityDeathEvent
import org.bukkit.event.Listener

import net.kyori.adventure.text.Component

import kotlin.random.Random

class PufferfishRainManager(
  private val plugin: Plugin,
  private val pufferfishCount: Int,
  private val explosionPower: Double,
  private val explosionPowerInWater: Double,
) : AbstractFeature(), Listener {

  companion object : FeatureContainerFactory<FeatureContainer> {

    override fun create(state: BuilderState): FeatureContainer {
      val manager =
        PufferfishRainManager(
          plugin = state.plugin,
          pufferfishCount = state.config.getInt("pufferfish.pufferfish_count"),
          explosionPower = state.config.getDouble("pufferfish.explosion_power"),
          explosionPowerInWater = state.config.getDouble("pufferfish.explosion_power_in_water"),
        )
      return FeatureBuilder()
        .addFeature(manager)
        .addListener(manager)
        .addRandomEvent(manager.pufferfishRainEvent)
        .build()
    }

    fun initializePufferfish(pufferfish: PufferFish) {
      pufferfish.setHealth(1.0)
    }

    fun spawnPufferfishOn(player: Player) {
      val world = player.getWorld()
      val targetLocation = player.getLocation().add(0.0, 15.0, 0.0)
      repeat(5) {
        val thisLocation = targetLocation.clone().add(Random.nextDouble(-1.0, 1.0), 0.0, Random.nextDouble(-1.0, 1.0))
        val pufferfish = world.spawn(thisLocation, PufferFish::class.java)
        pufferfish.setPuffState(2)
      }
    }

  }

  private inner class PufferfishRainEvent() : NotifiedRandomEvent(plugin) {
    override val name = "pufferfish"
    override val baseWeight = 0.5
    override val deltaWeight = 0.25

    override val messages: List<Component> = listOf(
      Component.text("It's raining... It's pouring..."),
      Component.text("The pufferfish are soaring..."),
    )

    override fun canFire(state: RandomEventState): Boolean =
      true

    override fun onAfterDelay(state: RandomEventState) {
      val onlinePlayers = Bukkit.getOnlinePlayers()
      for (player in onlinePlayers) {
        spawnPufferfishOn(player)
      }
    }
  }

  override val name: String = "pufferfish"

  override val description: String = "Pufferfish rain on all players periodically"

  val pufferfishRainEvent: RandomEvent =
    PufferfishRainEvent()
      .withTitle("Pufferfish Rain!")
      .withCooldown(24)
      .boundToFeature(this)

  @EventHandler
  fun onEntityDeath(event: EntityDeathEvent) {
    if (!isEnabled()) {
      return
    }

    val entity = event.getEntity()
    if (entity is PufferFish) {
      entity.world.createExplosion(entity.location, explosionPower(entity), false, false, entity)
    }

  }

  @EventHandler
  fun onEntityDamage(event: EntityDamageEvent) {
    if (!isEnabled()) {
      return
    }

    val entity = event.getEntity()

    // Protect pufferfish death by some effects
    if (entity is PufferFish) {
      if ((event.getCause() == EntityDamageEvent.DamageCause.DROWNING) || (event.getCause() == EntityDamageEvent.DamageCause.DRYOUT) || (event.getCause() == EntityDamageEvent.DamageCause.FALL)) {
        event.setCancelled(true)
      }
    }

  }

  private fun explosionPower(entity: Entity): Float =
    if (entity.isInWater) {
      explosionPowerInWater.toFloat()
    } else {
      explosionPower.toFloat()
    }

}
