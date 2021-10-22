
package com.mercerenies.turtletroll

import com.mercerenies.turtletroll.feature.RunnableFeature

import org.bukkit.entity.Player
import org.bukkit.plugin.Plugin
import org.bukkit.Bukkit
import org.bukkit.entity.Phantom
import org.bukkit.entity.EntityType
import org.bukkit.event.Listener
import org.bukkit.event.EventHandler
import org.bukkit.event.entity.EntityDamageEvent

import kotlin.collections.HashMap
import kotlin.random.Random

class PetPhantomManager(
  plugin: Plugin,
  val spawnChance: Double = 0.33,
  val random: Random = Random.Default,
) : RunnableFeature(plugin), Listener {

  companion object {
    val TICKS_PER_SECOND = 20
    val SECONDS_COOLDOWN_AFTER_KILL = 600

    val MIN_SPAWN_HEIGHT = 6
    val MAX_SPAWN_HEIGHT = 20

    val MAX_DISTANCE_SQUARED = 4096.0

    private fun shouldRespawn(player: Player, phantom: Phantom): Boolean {
      val dz = player.location.z - phantom.location.z
      val dx = player.location.x - phantom.location.x
      return (player.world != phantom.world) || (dx * dx + dz * dz > MAX_DISTANCE_SQUARED)
    }

  }

  private val safePlayers = CooldownMemory<Player>(plugin);

  private val knownPhantoms = HashMap<Player, Phantom>();

  override val name = "phantoms"

  override val description = "Everyone gets a pet phantom"

  override val taskPeriod = 3L * TICKS_PER_SECOND

  override fun run() {
    if (!isEnabled()) {
      return
    }

    for (player in Bukkit.getOnlinePlayers()) {
      val phantom = knownPhantoms[player]
      if ((phantom == null) || (shouldRespawn(player, phantom))) {
        if ((random.nextDouble() < spawnChance) && (!safePlayers.contains(player))) {
          spawnPhantom(player)
        }
      } else {
        phantom.target = player
        if (phantom.health <= 0) {
          knownPhantoms.remove(player)
          // The phantom died, so give the player a cooldown
          safePlayers.add(player, (TICKS_PER_SECOND * SECONDS_COOLDOWN_AFTER_KILL).toLong())
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
