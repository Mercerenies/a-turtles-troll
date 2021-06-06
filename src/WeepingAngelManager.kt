
package com.mercerenies.turtletroll

import com.mercerenies.turtletroll.feature.Feature

import org.bukkit.scheduler.BukkitRunnable
import org.bukkit.entity.Player
import org.bukkit.entity.ArmorStand
import org.bukkit.entity.LivingEntity
import org.bukkit.plugin.Plugin
import org.bukkit.Location
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.event.Listener
import org.bukkit.event.EventHandler
import org.bukkit.event.player.PlayerMoveEvent
import org.bukkit.event.entity.EntityDeathEvent

import kotlin.collections.HashMap

class WeepingAngelManager(
  val plugin: Plugin,
  val movementSpeed: Double = 1.0, // Meters per tick
) : BukkitRunnable(), Feature, Listener {
  private var activeAngels = HashMap<ArmorStand, AngelInfo>()

  private var _enabled: Boolean = true

  private data class AngelInfo(
    val target: Player,
  )

  companion object {
    val TICKS_PER_SECOND = 20

    val DISTANCE_SQUARED_THRESHOLD = 1.0
    val DEATH_SQUARED_THRESHOLD = 1.5
    val TOUCHING_SQUARED_THRESHOLD = 0.75

    fun getAllAngels(): List<ArmorStand> =
      Bukkit.getWorlds().flatMap { it.getEntitiesByClass(ArmorStand::class.java) }

    fun getAngelInLineOfSight(angels: List<ArmorStand>, entity: LivingEntity): ArmorStand? {
      for (block in entity.getLineOfSight(null, 32)) {
        for (angel in angels) {
          if (angel.world != entity.world) {
            continue
          }
          if (angel.location.distanceSquared(block.location) < DISTANCE_SQUARED_THRESHOLD) {
            if (angel.location.distanceSquared(entity.location) > DEATH_SQUARED_THRESHOLD) {
              return angel
            }
          }
        }
      }
      return null
    }

    fun getAngelInLineOfSight(entity: LivingEntity): ArmorStand? =
      getAngelInLineOfSight(getAllAngels(), entity)

  }

  override val name = "weepingangel"

  override val description = "Armor stands move when you're not looking"

  override fun enable() {
    _enabled = true
  }

  override fun disable() {
    _enabled = false
  }

  override fun isEnabled() = _enabled

  override fun run() {
    if (!isEnabled()) {
      return
    }

    val allAngels = getAllAngels()
    val onlinePlayers = Bukkit.getOnlinePlayers().toSet()

    // Safe angels are those being looked at by at least one player
    val safeAngels = HashSet<ArmorStand>()
    for (player in onlinePlayers) {
      val lookingAt = getAngelInLineOfSight(allAngels, player)
      if (lookingAt != null) {
        safeAngels.add(lookingAt)
      }
    }

    // Now we iterate over all active angels which are not safe
    val iter = activeAngels.entries.iterator()
    while (iter.hasNext()) {
      val entry = iter.next()
      val angel = entry.key
      val info = entry.value

      // Purge dead angels
      if (angel.health <= 0.0) {
        iter.remove()
        continue
      }

      val targetVec = info.target.location.clone().subtract(angel.location).toVector()
      if (targetVec.lengthSquared() < TOUCHING_SQUARED_THRESHOLD) {
        // We're close enough to damage the player (we can do this even if we're safe)
        info.target.damage(5.0, angel) // TODO Get a custom name?
        if (info.target.health <= 0.0) {
          iter.remove()
          continue
        }
      }

      if (safeAngels.contains(angel)) {
        continue
      }

      // If the player has logged off or is in a different world, then
      // cancel the attack
      if ((!onlinePlayers.contains(info.target)) || (info.target.world != angel.world)) {
        iter.remove()
        continue
      }

      // Face and move toward the player
      val yaw = Math.toDegrees(Math.atan2(- targetVec.getX(), targetVec.getZ())).toFloat()
      val pitch = Math.toDegrees(Math.atan2(- targetVec.getY(), Math.sqrt(targetVec.getX() * targetVec.getX() + targetVec.getZ() * targetVec.getZ()))).toFloat()
      angel.setRotation(yaw, pitch)
      angel.setVelocity(targetVec.normalize().multiply(movementSpeed))
    }

  }

  fun register() {
    this.runTaskTimer(plugin, 1L, TICKS_PER_SECOND / 4L)
  }

  @EventHandler
  fun onPlayerMove(event: PlayerMoveEvent) {
    if (!isEnabled()) {
      return
    }
    val player = event.player
    val angel = getAngelInLineOfSight(player)
    if (angel != null) {
      // This might overwrite previous information, which is fine. If
      // the angel was previously targeting someone else, it should
      // aim for the new target.
      activeAngels[angel] = AngelInfo(player)
    }
  }

  @EventHandler
  fun onEntityDeath(event: EntityDeathEvent) {
    val entity = event.entity
    if (entity is ArmorStand) {
      if (activeAngels.contains(entity)) {
        activeAngels.remove(entity)
      }
    }
  }

}
