
package com.mercerenies.turtletroll.pot

import com.mercerenies.turtletroll.util.component.*
import com.mercerenies.turtletroll.gravestone.CustomDeathMessageRegistry
import com.mercerenies.turtletroll.gravestone.CustomDeathMessage
import com.mercerenies.turtletroll.gravestone.Mimic
import com.mercerenies.turtletroll.feature.AbstractFeature
import com.mercerenies.turtletroll.SpawnReason
import com.mercerenies.turtletroll.Constants
import com.mercerenies.turtletroll.location.BlockSelector

import org.bukkit.entity.Player
import org.bukkit.entity.EntityType
import org.bukkit.plugin.Plugin
import org.bukkit.Material
import org.bukkit.event.Listener
import org.bukkit.event.EventHandler
import org.bukkit.event.block.Action
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryOpenEvent
import org.bukkit.event.inventory.InventoryType
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.event.entity.CreatureSpawnEvent
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.scheduler.BukkitRunnable
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack

import net.kyori.adventure.text.Component

import kotlin.random.Random

class PotteryListener(
  val plugin: Plugin,
  private val mobReplaceChance: Double,
) : AbstractFeature(), Listener {

  companion object {
    val SAFETY_RADIUS = 8

    val MOBS_TO_REPLACE = setOf(
      EntityType.ZOMBIE, EntityType.SKELETON, EntityType.SPIDER, EntityType.ZOMBIFIED_PIGLIN,
      EntityType.STRAY, EntityType.HUSK,
    )
  }

  override val name = "pottery"

  override val description = "Decorated pots will sometimes spawn in the wild"

  private val potIdentifier = PotteryIdentifier(plugin)

  val breakAction: PotteryBreakAction = PotteryBreakAction(this, potIdentifier)

  @EventHandler
  fun onCreatureSpawn(event: CreatureSpawnEvent) {
    if (!isEnabled()) {
      return
    }
    if (!SpawnReason.isNatural(event)) {
      return
    }
    if (MOBS_TO_REPLACE.contains(event.entity.type)) {
      val nearbyMatching = BlockSelector.countNearbyMatching(event.location.block, SAFETY_RADIUS) {
        it.type == Material.DECORATED_POT
      }
      if (nearbyMatching < 1) {
        if (Random.nextDouble() < mobReplaceChance) {
          val below = event.location.clone().add(0.0, -1.0, 0.0)
          if (below.block.type != Material.AIR) {
            event.setCancelled(true)
            potIdentifier.spawnPottery(event.location.block)
          }
        }
      }
    }
  }
}
