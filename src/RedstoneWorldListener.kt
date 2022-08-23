
package com.mercerenies.turtletroll

import com.mercerenies.turtletroll.gravestone.CustomDeathMessageRegistry
import com.mercerenies.turtletroll.gravestone.CustomDeathMessage
import com.mercerenies.turtletroll.gravestone.Redstone
import com.mercerenies.turtletroll.feature.AbstractFeature
import com.mercerenies.turtletroll.feature.container.FeatureContainer
import com.mercerenies.turtletroll.feature.container.ListenerContainer
import com.mercerenies.turtletroll.feature.builder.BuilderState
import com.mercerenies.turtletroll.feature.builder.FeatureContainerFactory
import com.mercerenies.turtletroll.util.lerp
import com.mercerenies.turtletroll.util.clamp

import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDeathEvent
import org.bukkit.event.entity.EntityPickupItemEvent
import org.bukkit.entity.Entity
import org.bukkit.entity.EntityType
import org.bukkit.inventory.ItemStack
import org.bukkit.Material
import org.bukkit.entity.Player

import kotlin.random.Random
import kotlin.math.ln
import kotlin.math.min
import kotlin.math.max

class RedstoneWorldListener(
  private val deathRegistry: CustomDeathMessageRegistry,
  val dropChance: Double = 0.5,
  val minItems: Int = 1,
  val maxItems: Int = 64,
) : AbstractFeature(), Listener {

  override val name = "redstoneworld"

  override val description = "Several mobs will drop redstone, picking up redstone deals instant damage"

  @EventHandler
  fun onEntityDeath(event: EntityDeathEvent) {
    if (!isEnabled()) {
      return
    }
    if (Random.nextDouble() < dropChance) {
      val count = numberToDrop()
      event.getDrops().add(ItemStack(Material.REDSTONE, count))
    }
  }

  private fun numberToDrop(): Int {
    val rand = Random.nextDouble()
    val exp = min(- ln(rand), 10.0) / 10.0
    val n = lerp(minItems.toDouble(), maxItems.toDouble(), exp)
    return (n + 0.5).toInt()
  }

  @EventHandler
  fun onEntityPickupItemEvent(event: EntityPickupItemEvent) {
    if (!isEnabled()) {
      return
    }
    val entity = event.entity
    if (entity !is Player) {
      return
    }

    val itemStack = event.item.itemStack
    if (itemStack.type == Material.REDSTONE) {
      dealDamage(entity, event.item, itemStack.amount)
    }

  }

  private fun dealDamage(player: Player, damager: Entity?, redstoneAmount: Int) {
    val lerpAmount = clamp(redstoneAmount / 64.0, 0.0, 1.0)
    val damageAmount = max(lerp(0.0, 20.0, lerpAmount), 1.0)
    val customMessage = CustomDeathMessage(
      Redstone,
      "${player.getDisplayName()} picked up some strange dust.",
    )
    deathRegistry.withCustomDeathMessage(customMessage) {
      player.damage(damageAmount, damager)
    }
  }

}
