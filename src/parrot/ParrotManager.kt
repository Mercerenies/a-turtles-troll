
package com.mercerenies.turtletroll.parrot

import com.mercerenies.turtletroll.Constants
import com.mercerenies.turtletroll.NameSource
import com.mercerenies.turtletroll.Messages
import com.mercerenies.turtletroll.CooldownMemory
import com.mercerenies.turtletroll.feature.RunnableFeature
import com.mercerenies.turtletroll.feature.container.FeatureContainer
import com.mercerenies.turtletroll.feature.container.ManagerContainer
import com.mercerenies.turtletroll.feature.builder.BuilderState
import com.mercerenies.turtletroll.feature.builder.FeatureContainerFactory
import com.mercerenies.turtletroll.location.PlayerSelector
import com.mercerenies.turtletroll.util.component.*

import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerMoveEvent
import org.bukkit.event.entity.EntitySpawnEvent
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import org.bukkit.entity.HumanEntity
import org.bukkit.entity.Parrot
import org.bukkit.entity.Player
import org.bukkit.plugin.Plugin
import org.bukkit.attribute.Attribute
import org.bukkit.Bukkit

import net.kyori.adventure.text.Component

class ParrotManager(_plugin: Plugin) : RunnableFeature(_plugin), Listener {

  companion object : FeatureContainerFactory<FeatureContainer> {

    val DISTANCE_SQUARED_LIMIT = 16384.0 // 128 blocks (squared)

    override fun create(state: BuilderState): FeatureContainer =
      ManagerContainer(ParrotManager(state.plugin))

    private val TARGET_EFFECT_TYPE: PotionEffectType = PotionEffectType.LEVITATION

    fun getAllParrots(): List<Parrot> =
      Bukkit.getWorlds().flatMap {
        it.getEntitiesByClass(Parrot::class.java)
      }

    @Suppress("DEPRECATION")
    private fun getPerchedParrot(entity: HumanEntity): Parrot? {
      val left = entity.getShoulderEntityLeft()
      val right = entity.getShoulderEntityRight()
      if (left is Parrot) {
        return left
      } else if (right is Parrot) {
        return right
      } else {
        return null
      }
    }

    private fun attackMessage(parrot: Parrot): Component =
      Component.text("<").append(parrot.customName() ?: Component.text("Parrot")).append("> SQUAAAAAAWK! Yer comin' with me!")

    val DEFAULT_NAME_SOURCE = NameSource.FromList(
      listOf(
        // Parrots from media
        "Iago", "Polly", "Captain Flint", "Blu", "Jewel",
        "Paulie", "Captain Celaeno", "Short Tom", "Jose Carioca",
        "Mr. Dinsdale", "Captain Clawbeak", "Hooey",
      )
    )

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
      parrot.owner = PlayerSelector.findNearestPlayer(parrot, DISTANCE_SQUARED_LIMIT)
      if (parrot.customName() == null) {
        parrot.customName(nameSource.sampleName())
      }
      val maxHealth = parrot.getAttribute(Attribute.MAX_HEALTH)?.getValue()
      if (maxHealth != null) {
        parrot.setHealth(maxHealth)
      }
    }
  }

  @Suppress("DEPRECATION")
  private fun checkPlayer(player: Player) {
    val parrot = getPerchedParrot(player)
    if (parrot != null) {
      if (!safePlayers.contains(player)) {
        if (!player.hasPotionEffect(TARGET_EFFECT_TYPE)) {
          Messages.sendMessage(player, attackMessage(parrot))
          player.addPotionEffect(PotionEffect(TARGET_EFFECT_TYPE, Constants.TICKS_PER_SECOND * 3, 100))
          player.setShoulderEntityLeft(null)
          player.setShoulderEntityRight(null)
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
