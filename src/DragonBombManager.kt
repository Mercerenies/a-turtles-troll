
package com.mercerenies.turtletroll

import com.mercerenies.turtletroll.feature.RunnableFeature
import com.mercerenies.turtletroll.feature.container.FeatureContainer
import com.mercerenies.turtletroll.feature.container.ManagerContainer
import com.mercerenies.turtletroll.feature.builder.BuilderState
import com.mercerenies.turtletroll.feature.builder.FeatureContainerFactory
import com.mercerenies.turtletroll.ext.*

import org.bukkit.plugin.Plugin
import org.bukkit.Bukkit
import org.bukkit.NamespacedKey
import org.bukkit.entity.EnderDragon
import org.bukkit.entity.EnderCrystal
import org.bukkit.entity.TNTPrimed
import org.bukkit.event.Listener
import org.bukkit.event.EventHandler
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.persistence.PersistentDataType
import org.bukkit.attribute.Attribute

class DragonBombManager(
  plugin: Plugin,
  private val minTimerTriggersPerAttack: Int,
  private val maxTimerTriggersPerAttack: Int,
) : RunnableFeature(plugin), Listener {

  companion object : FeatureContainerFactory<FeatureContainer> {
    val BOMB_MARKER_KEY = "dragon_bomb_manager_tag"
    val VELOCITY_EPSILON = 0.0001

    override fun create(state: BuilderState): FeatureContainer =
      ManagerContainer(
        DragonBombManager(
          plugin = state.plugin,
          minTimerTriggersPerAttack = state.config.getInt("dragonbomb.min_timer_triggers_per_attack"),
          maxTimerTriggersPerAttack = state.config.getInt("dragonbomb.max_timer_triggers_per_attack"),
        )
      )

  }

  private var timerTick = 0L
  private val cooldownMemory = CooldownMemory<TNTPrimed>(plugin)

  override val name = "dragonbomb"

  override val description = "The Ender Dragon drops TNT regularly"

  override val taskPeriod = 5L

  val markerKey = NamespacedKey(plugin, BOMB_MARKER_KEY)

  override fun run() {
    if (!isEnabled()) {
      return
    }
    timerTick += 1L
    doDragonAttack()
    checkAllTNT()
  }

  @EventHandler
  fun onEntityDamage(event: EntityDamageEvent) {
    if (!isEnabled()) {
      return
    }

    val entity = event.getEntity()

    // Protect dragon from all explosions
    if ((entity is EnderDragon) || (entity is EnderCrystal)) {
      if ((event.getCause() == EntityDamageEvent.DamageCause.BLOCK_EXPLOSION) || (event.getCause() == EntityDamageEvent.DamageCause.ENTITY_EXPLOSION)) {
        event.setCancelled(true)
      }
    }

  }

  private fun currentTriggersPerAttack(dragon: EnderDragon): Long {
    val maxHealth = dragon.getAttribute(Attribute.GENERIC_MAX_HEALTH)!!.getValue()
    val healthFraction = dragon.getHealth().toDouble() / maxHealth
    val triggers = healthFraction * maxTimerTriggersPerAttack + (1 - healthFraction) * minTimerTriggersPerAttack
    return triggers.toLong()
  }

  private fun doDragonAttack() {
    val server = Bukkit.getServer()
    for (world in server.getWorlds()) {
      for (dragon in world.getEntitiesByClass(EnderDragon::class.java)) {
        if (timerTick % currentTriggersPerAttack(dragon) == 0L) {
          val tnt = world.spawn(dragon.getLocation(), TNTPrimed::class.java)
          tnt.setFuseTicks((Constants.TICKS_PER_SECOND * 10).toInt())

          val container = tnt.getPersistentDataContainer()
          container.set(markerKey, PersistentDataType.INTEGER, 1)

          cooldownMemory.add(tnt, 4L)

        }
      }
    }
  }

  private fun checkAllTNT() {
    // Any TNT with the marker key set which is not moving should
    // explode.
    val server = Bukkit.getServer()
    for (world in server.getWorlds()) {
      for (tnt in world.getEntitiesByClass(TNTPrimed::class.java)) {
        if (!cooldownMemory.contains(tnt)) {
          if (tnt.getVelocity().length() <= VELOCITY_EPSILON) {
            if (tnt.getPersistentDataContainer().get(markerKey, PersistentDataType.INTEGER) == 1) {
              tnt.setFuseTicks(1) // Go boom :)
            }
          }
        }
      }
    }
  }

}
