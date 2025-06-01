
package com.mercerenies.turtletroll

import com.mercerenies.turtletroll.feature.RunnableFeature
import com.mercerenies.turtletroll.feature.container.FeatureContainer
import com.mercerenies.turtletroll.feature.container.ManagerContainer
import com.mercerenies.turtletroll.feature.builder.BuilderState
import com.mercerenies.turtletroll.feature.builder.FeatureContainerFactory
import com.mercerenies.turtletroll.nms.NMS
import com.mercerenies.turtletroll.location.PlayerSelector
import com.mercerenies.turtletroll.util.component.*

import io.papermc.paper.event.player.PrePlayerAttackEntityEvent

import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntitySpawnEvent
import org.bukkit.event.entity.EntityDeathEvent
import org.bukkit.potion.PotionEffectType
import org.bukkit.entity.Allay
import org.bukkit.entity.Player
import org.bukkit.plugin.Plugin
import org.bukkit.inventory.ItemStack
import org.bukkit.damage.DamageSource
import org.bukkit.damage.DamageType
import org.bukkit.Bukkit
import org.bukkit.Material

import net.kyori.adventure.text.Component

class AllayManager(_plugin: Plugin) : RunnableFeature(_plugin), Listener {

  companion object : FeatureContainerFactory<FeatureContainer> {

    val GOLDEN_SWORD = Material.GOLDEN_SWORD

    val DISTANCE_SQUARED_LIMIT = 16384.0 // 128 blocks (squared)

    val DEATH_RADIUS_SQUARED = 256.0 // 16 blocks (squared)

    override fun create(state: BuilderState): FeatureContainer =
      ManagerContainer(AllayManager(state.plugin))

    private val targetEffectType: PotionEffectType = PotionEffectType.LEVITATION

    fun getAllAllays(): List<Allay> =
      Bukkit.getWorlds().flatMap {
        it.getEntitiesByClass(Allay::class.java)
      }

    private fun allayCleanseMessage(player: Player): Component =
      Component.text("")
        .append(player.displayName())
        .append(" has cleansed themself and is now freed from their allays")

  }

  override val name = "allays"

  override val description = "Allays constantly want to deliver pretty flowers to everybody around"

  override val taskPeriod = 2L * Constants.TICKS_PER_SECOND + 2L

  private fun manageAllay(allay: Allay) {
    val nearestPlayerToAllay = PlayerSelector.findNearestPlayer(allay, DISTANCE_SQUARED_LIMIT)
    if (nearestPlayerToAllay != null) {
      NMS.setAllayFriend(allay, nearestPlayerToAllay)
    }

    val inventory = allay.inventory
    val item = inventory.getItem(0)
    if ((item == null) || (!BlockTypes.FLOWERS.contains(item.type))) {
      // Make sure the allay is holding flowers.
      val flowerType = BlockTypes.FLOWERS.random()
      inventory.setItem(0, ItemStack(flowerType, 64))
      allay.equipment.setItemInMainHand(ItemStack(flowerType, 64))
    }

  }

  override fun run() {
    if (!isEnabled()) {
      return
    }

    for (allay in getAllAllays()) {
      manageAllay(allay)
    }

  }

  @EventHandler
  fun onEntitySpawn(event: EntitySpawnEvent) {
    if (!isEnabled()) {
      return
    }

    val entity = event.entity
    if (entity is Allay) {
      manageAllay(entity)
    }

  }

  // The game fires attacks on friendly allays as canceled, so we have
  // to catch it in the pre-attack event.
  @EventHandler
  fun onPrePlayerAttackEntity(event: PrePlayerAttackEntityEvent) {
    if (!isEnabled()) {
      return
    }

    val attackedEntity = event.attacked
    if (attackedEntity is Allay) {
      val weapon = event.player.inventory.getItemInMainHand()
      if (weapon.type == GOLDEN_SWORD) {
        attackedEntity.damage(99999.0)
      }
    }
  }

  @EventHandler
  fun onEntityDeath(event: EntityDeathEvent) {
    if (!isEnabled()) {
      return
    }
    val entity = event.entity
    if ((entity is Player) && (event.damageSource.damageType == DamageType.LIGHTNING_BOLT)) {
      val nearbyAllays = entity.world.getEntitiesByClass(Allay::class.java)
          .filter { it.location.clone().subtract(entity.location).lengthSquared() < DEATH_RADIUS_SQUARED }
      for (allay in nearbyAllays) {
        allay.damage(99999.0)
      }
      if (!nearbyAllays.isEmpty()) {
        Messages.broadcastMessage(allayCleanseMessage(entity))
      }
    }
  }

}
