
package com.mercerenies.turtletroll

import com.mercerenies.turtletroll.feature.RunnableFeature

import org.bukkit.entity.Player
import org.bukkit.entity.PufferFish
import org.bukkit.plugin.Plugin
import org.bukkit.Bukkit
import org.bukkit.World
import org.bukkit.event.EventHandler
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.event.Listener

import kotlin.random.Random

class PufferfishRainManager(val plugin: Plugin) : RunnableFeature(), Listener {

  companion object {
    val TICKS_PER_SECOND = 20L
    val WARNING_TIME = 5700L
    val DROP_TIME = 6000L

    fun getSystemTime(): Long {
      for (world in Bukkit.getServer().getWorlds()) {
        if (world.environment == World.Environment.NORMAL) {
          return world.getTime()
        }
      }
      return 0L // That's not good :(
    }

    fun spawnPufferfishOn(player: Player) {
      val world = player.getWorld()
      val targetLocation = player.getLocation().add(0.0, 15.0, 0.0)
      repeat(5) {
        val thisLocation = targetLocation.clone().add(Random.nextDouble(-1.0, 1.0), 0.0, Random.nextDouble(-1.0, 1.0));
        val pufferfish = world.spawn(thisLocation, PufferFish::class.java)
        pufferfish.setPuffState(2)
      }
    }

  }

  private enum class State {
    Idle, // Waiting for 11:59
    Warned, // Between 11:59 and 12:00
  }

  override val name: String = "pufferfish"

  override val description: String = "Pufferfish rain on all players at noon"

  private var state: State = State.Idle

  override fun run() {
    if (!isEnabled()) {
      return
    }

    val time = getSystemTime()

    when (state) {

      State.Idle -> {
        if ((time > WARNING_TIME) && (time < DROP_TIME)) {
          state = State.Warned
          Bukkit.broadcastMessage("It's raining... It's pouring...")
          Bukkit.broadcastMessage("The pufferfish are touring...")
        }
      }

      State.Warned -> {
        if ((time < WARNING_TIME) || (time > DROP_TIME)) {
          state = State.Idle;
          val onlinePlayers = Bukkit.getOnlinePlayers();
          for (player in onlinePlayers) {
            spawnPufferfishOn(player);
          }
        }
      }

    }

  }

  fun register() {
    this.runTaskTimer(plugin, TICKS_PER_SECOND * 5L, TICKS_PER_SECOND * 5L)
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

}
