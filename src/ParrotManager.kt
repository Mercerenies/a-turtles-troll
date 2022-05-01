
package com.mercerenies.turtletroll

import com.mercerenies.turtletroll.feature.RunnableFeature
import com.mercerenies.turtletroll.feature.container.FeatureContainer
import com.mercerenies.turtletroll.feature.container.ManagerContainer
import com.mercerenies.turtletroll.feature.builder.BuilderState
import com.mercerenies.turtletroll.feature.builder.FeatureContainerFactory
import com.mercerenies.turtletroll.nms.NMS
import com.mercerenies.turtletroll.location.PlayerSelector

import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerMoveEvent
import org.bukkit.event.entity.EntitySpawnEvent
import org.bukkit.event.entity.EntityDeathEvent
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import org.bukkit.entity.EntityType
import org.bukkit.entity.Entity
import org.bukkit.entity.Parrot
import org.bukkit.entity.Player
import org.bukkit.plugin.Plugin
import org.bukkit.attribute.Attribute
import org.bukkit.Bukkit

class ParrotManager(_plugin: Plugin): RunnableFeature(_plugin), Listener {

  companion object : FeatureContainerFactory<FeatureContainer> {

    val DISTANCE_SQUARED_LIMIT = 16384.0 // 128 blocks (squared)

    override fun create(state: BuilderState): FeatureContainer =
      ManagerContainer(ParrotManager(state.plugin))

    private val targetEffectType: PotionEffectType = PotionEffectType.LEVITATION

    fun getAllParrots(): List<Parrot> =
      Bukkit.getWorlds().flatMap {
        it.getEntitiesByClass(Parrot::class.java)
      }

    fun nearestPlayer(entity: Entity): Player? =
      PlayerSelector.findNearestPlayer(entity.location, DISTANCE_SQUARED_LIMIT)

    val DEFAULT_NAME_SOURCE = NameSource.FromList(listOf(
      "Iago", "Polly", "Captain Flint", "Blu",
    ))

  }

  override val name = "parrots"

  override val description = "Parrots are automatically tamed and will try to fly off with their owner"

  override val taskPeriod = 2L * Constants.TICKS_PER_SECOND + 1L

  private val safePlayers = CooldownMemory<Player>(plugin)

  // Might turn this into a constructor arg later
  private val nameSource = DEFAULT_NAME_SOURCE

  private fun manageParrot(parrot: Parrot) {
    parrot.addPotionEffect(PotionEffect(PotionEffectType.SPEED, Constants.TICKS_PER_SECOND * 999, 1))
    parrot.addPotionEffect(PotionEffect(PotionEffectType.HEALTH_BOOST, Constants.TICKS_PER_SECOND * 999, 2))
    parrot.setSitting(false)
    if (parrot.owner == null) {
      parrot.owner = nearestPlayer(parrot)
      if (parrot.customName == null) {
        parrot.customName = nameSource.sampleName()
      }
      val maxHealth = parrot.getAttribute(Attribute.GENERIC_MAX_HEALTH)?.getValue()
      if (maxHealth != null) {
        parrot.setHealth(maxHealth)
      }
    }
  }

  private fun checkPlayer(player: Player) {
    val info = NMS.getPlayerParrotInfo(player)
    val hasPerchedParrot = info.hasAnyShoulderPerch()
    if (hasPerchedParrot) {
      if (!safePlayers.contains(player)) {
        if (!player.hasPotionEffect(targetEffectType)) {
          player.sendMessage("SQUAAAAAAWK! Yer comin' with me!") // TODO Get the parrot's name (from NMS) for this message
          player.addPotionEffect(PotionEffect(PotionEffectType.LEVITATION, Constants.TICKS_PER_SECOND * 3, 100))
          safePlayers.add(player, Constants.TICKS_PER_SECOND * 10L)
        }
      }
    }
  }

  override fun run() {
    if (!isEnabled()) {
      return
    }

    for (parrot in getAllParrots()) {
      // Parrots do not sit. Parrots fly away into the night gallantly
      // with their owner in tow.
      manageParrot(parrot)
    }

    for (player in Bukkit.getOnlinePlayers()) {
      checkPlayer(player)
    }

  }

  @EventHandler
  fun onEntitySpawn(event: EntitySpawnEvent) {
    if (!isEnabled()) {
      return
    }

    val entity = event.entity
    if (entity is Parrot) {
      manageParrot(entity)
    }

  }

  @EventHandler
  fun onPlayerMove(event: PlayerMoveEvent) {
    if (!isEnabled()) {
      return
    }
    val player = event.player
    checkPlayer(player)
  }

}
