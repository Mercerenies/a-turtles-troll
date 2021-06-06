
package com.mercerenies.turtletroll

import com.mercerenies.turtletroll.feature.Feature

import org.bukkit.scheduler.BukkitRunnable
import org.bukkit.entity.Player
import org.bukkit.plugin.Plugin
import org.bukkit.Location
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.entity.Phantom
import org.bukkit.entity.EntityType
import org.bukkit.event.Listener
import org.bukkit.event.EventHandler
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.event.entity.EntityDamageByEntityEvent

import kotlin.collections.HashMap
import kotlin.random.Random

class PetPhantomManager(
  val plugin: Plugin,
  val spawnChance: Double = 0.33,
  val random: Random = Random.Default,
) : BukkitRunnable(), Feature, Listener {

  companion object {
    val TICKS_PER_SECOND = 20

    val MIN_SPAWN_HEIGHT = 6
    val MAX_SPAWN_HEIGHT = 20

    val MAX_DISTANCE_SQUARED = 65536

    private fun shouldRespawn(player: Player, phantom: Phantom): Boolean {
      return (player.world != phantom.world) || (player.location.distanceSquared(phantom.location) > MAX_DISTANCE_SQUARED)
    }

  }

  private val knownPhantoms = HashMap<Player, Phantom>();

  private var _enabled: Boolean = true

  override val name = "phantoms"

  override val description = "Everyone gets a pet phantom"

  override fun enable() {
    _enabled = true
  }

  override fun disable() {
    _enabled = false
  }

  override fun isEnabled() = _enabled

  fun register() {
    this.runTaskTimer(plugin, 1L, 3L * TICKS_PER_SECOND)
  }

  override fun run() {
    if (!isEnabled()) {
      return
    }

    for (player in Bukkit.getOnlinePlayers()) {
      val phantom = knownPhantoms[player]
      if ((phantom == null) || (shouldRespawn(player, phantom))) {
        if (random.nextDouble() < spawnChance) {
          spawnPhantom(player)
        }
      } else {
        phantom.target = player
        // println(player.location.distance(phantom.location))
        if (phantom.health <= 0) {
          knownPhantoms.remove(player)
        }
      }
    }

  }

  @EventHandler
  fun onEntityDamage(event: EntityDamageEvent) {
    if (!isEnabled()) {
      return
    }

    val entity = event.getEntity()

    // Protect phantoms from fire
    if (entity is Phantom) {
      if (knownPhantoms.containsValue(entity)) {
        if ((event.getCause() == EntityDamageEvent.DamageCause.FIRE) || (event.getCause() == EntityDamageEvent.DamageCause.FIRE_TICK)) {
          event.setCancelled(true)
        }
      }
    }

  }

  private fun spawnPhantom(player: Player): Phantom? {
    val loc = player.location.clone()
    loc.y += MIN_SPAWN_HEIGHT
    var maxDistLeft = (MAX_SPAWN_HEIGHT - MIN_SPAWN_HEIGHT)
    while ((maxDistLeft > 0) && (!loc.block.isEmpty())) {
      maxDistLeft -= 1;
      loc.y += 1;
    }
    return if (loc.block.isEmpty()) {
      val newPhantom = player.world.spawnEntity(loc, EntityType.PHANTOM) as Phantom
      newPhantom.target = player
      knownPhantoms[player] = newPhantom
      newPhantom
    } else {
      null
    }
  }

}