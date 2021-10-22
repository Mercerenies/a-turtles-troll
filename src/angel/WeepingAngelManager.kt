
package com.mercerenies.turtletroll.angel

import com.mercerenies.turtletroll.feature.RunnableFeature
import com.mercerenies.turtletroll.SpawnReason

import org.bukkit.entity.Player
import org.bukkit.entity.ArmorStand
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.EntityType
import org.bukkit.plugin.Plugin
import org.bukkit.Location
import org.bukkit.Color
import org.bukkit.Bukkit
import org.bukkit.Particle
import org.bukkit.event.Listener
import org.bukkit.event.EventHandler
import org.bukkit.event.player.PlayerMoveEvent
import org.bukkit.event.entity.PlayerDeathEvent
import org.bukkit.event.entity.EntityDeathEvent
import org.bukkit.event.entity.CreatureSpawnEvent
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.Sound
import org.bukkit.util.EulerAngle

import kotlin.collections.HashMap
import kotlin.random.Random

class WeepingAngelManager(
  plugin: Plugin,
  val movementSpeed: Double = 1.0, // Meters per tick
) : RunnableFeature(plugin), Listener {
  private var activeAngels = HashMap<ArmorStand, AngelInfo>()

  private data class AngelInfo(
    val target: Player,
  )

  companion object {

    val DISTANCE_SQUARED_THRESHOLD = 1.0
    val DEATH_SQUARED_THRESHOLD = 1.5
    val TOUCHING_SQUARED_THRESHOLD = 0.75

    val MAX_ANGELS_PER_CHUNK = 2
    val CHUNK_SIZE = 16

    val MOB_REPLACE_CHANCE = 0.05
    val MOBS_TO_REPLACE = setOf(
      EntityType.ZOMBIE, EntityType.SKELETON, EntityType.SPIDER, EntityType.ZOMBIFIED_PIGLIN,
      EntityType.STRAY, EntityType.HUSK,
    )

    fun getAllAngels(): List<ArmorStand> =
      Bukkit.getWorlds().flatMap { it.getEntitiesByClass(ArmorStand::class.java) }

    fun getNearbyAngels(baseLoc: Location): List<ArmorStand> =
      baseLoc.getWorld()!!.getEntitiesByClass(ArmorStand::class.java).filter { entity ->
        val distance = Math.abs(entity.location.x - baseLoc.x) + Math.abs(entity.location.z - baseLoc.z)
        distance < CHUNK_SIZE
      }

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

    fun assignIdlePose(angel: ArmorStand) {
      // left: -130, 50, 10
      // right: -130, -50, -10
      angel.setLeftArmPose(EulerAngle(2.269, -0.873, 0.175))
      angel.setRightArmPose(EulerAngle(2.269, 0.873, -0.175))
    }

    fun assignAttackPose(angel: ArmorStand) {
      angel.setLeftArmPose(EulerAngle(-90.0, 0.0, 0.0))
      angel.setRightArmPose(EulerAngle(-90.0, 0.0, 0.0))
    }

  }

  override val name = "weepingangel"

  override val description = "Armor stands move when you're not looking"

  override val taskPeriod = 5L

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
        val location = lookingAt.location.clone().add(0.0, 1.0, 0.0)
        val world = lookingAt.world
        world.spawnParticle(Particle.REDSTONE, location, 4, 0.25, 0.5, 0.25, Particle.DustOptions(Color.ORANGE, 1.0f))
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

      // If the player has logged off or is in a different world, then
      // cancel the attack
      if ((!onlinePlayers.contains(info.target)) || (info.target.world != angel.world)) {
        iter.remove()
        continue
      }

      val targetVec = info.target.location.clone().subtract(angel.location).toVector()
      if (targetVec.lengthSquared() < TOUCHING_SQUARED_THRESHOLD) {
        // We're close enough to damage the player (we can do this even if we're safe)
        info.target.damage(5.0, angel)
        if (info.target.health <= 0.0) {
          iter.remove()
          continue
        }
      }

      if (safeAngels.contains(angel)) {
        continue
      }

      // Face and move toward the player
      val yaw = Math.toDegrees(Math.atan2(- targetVec.getX(), targetVec.getZ())).toFloat()
      val pitch = Math.toDegrees(Math.atan2(- targetVec.getY(), Math.sqrt(targetVec.getX() * targetVec.getX() + targetVec.getZ() * targetVec.getZ()))).toFloat()
      angel.setRotation(yaw, pitch)
      angel.setVelocity(targetVec.normalize().multiply(movementSpeed))
      angel.world.playSound(angel.location, Sound.ENTITY_GHAST_SCREAM, 1.0f, 0.0f)
      assignAttackPose(angel)
    }

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
      assignIdlePose(angel)
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

  @EventHandler
  fun onCreatureSpawn(event: CreatureSpawnEvent) {
    if (!isEnabled()) {
      return
    }
    if (!SpawnReason.isNatural(event)) {
      return
    }
    if (MOBS_TO_REPLACE.contains(event.entity.type)) {
      if (getNearbyAngels(event.location).size < MAX_ANGELS_PER_CHUNK) {
        if (Random.nextDouble() < MOB_REPLACE_CHANCE) {
          event.setCancelled(true)
          val angel = ArmorStandSpawner.spawn(event.location)
          assignIdlePose(angel)
        }
      }
    }
  }

  @EventHandler
  fun onPlayerDeath(event: PlayerDeathEvent) {
    if (!isEnabled()) {
      return
    }
    val cause = event.entity.getLastDamageCause()
    if (cause is EntityDamageByEntityEvent) {
      val damager = cause.getDamager()
      if ((damager is ArmorStand) && (activeAngels.contains(damager))) {
        event.setDeathMessage("${event.entity.getDisplayName()} blinked.")
      }
    }
  }

}
