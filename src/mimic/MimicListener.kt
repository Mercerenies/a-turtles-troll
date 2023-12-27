
package com.mercerenies.turtletroll.mimic

import com.mercerenies.turtletroll.ext.*
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
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.event.entity.CreatureSpawnEvent
import org.bukkit.scheduler.BukkitRunnable

import net.kyori.adventure.text.Component

import kotlin.random.Random

class MimicListener(
  val plugin: Plugin,
  private val deathRegistry: CustomDeathMessageRegistry,
  private val mobReplaceChance: Double,
) : AbstractFeature(), Listener {

  companion object {
    val KILL_DELAY = Constants.TICKS_PER_SECOND

    val SAFETY_RADIUS = 8

    val MOBS_TO_REPLACE = setOf(
      EntityType.ZOMBIE, EntityType.SKELETON, EntityType.SPIDER, EntityType.ZOMBIFIED_PIGLIN,
      EntityType.STRAY, EntityType.HUSK,
    )

  }

  private inner class KillPlayerRunnable(val player: Player) : BukkitRunnable() {

    val message = CustomDeathMessage(
      Mimic,
      Component.text("").append(player.displayName()).append(" was eaten by a Mimic."),
    )

    override fun run() {
      deathRegistry.withCustomDeathMessage(message) {
        player.damage(9999.0, null)
      }
    }

  }

  override val name = "mimics"

  override val description = "Chests randomly spawn in the wild which, if opened, kill you"

  private val mimicIdentifier = MimicIdentifier(plugin)

  @EventHandler
  fun onCreatureSpawn(event: CreatureSpawnEvent) {
    if (!isEnabled()) {
      return
    }
    if (!SpawnReason.isNatural(event)) {
      return
    }
    if (MOBS_TO_REPLACE.contains(event.entity.type)) {
      val nearbyMatching = BlockSelector.countNearbyMatching(event.location.block, MimicListener.SAFETY_RADIUS) {
        BlockSelector.isMimicOrCake(mimicIdentifier, it)
      }
      if (nearbyMatching < 1) {
        if (Random.nextDouble() < mobReplaceChance) {
          val below = event.location.clone().add(0.0, -1.0, 0.0)
          if (below.block.type != Material.AIR) {
            event.setCancelled(true)
            mimicIdentifier.spawnMimic(event.location.block)
          }
        }
      }
    }
  }

  @EventHandler
  fun onPlayerInteract(event: PlayerInteractEvent) {
    if (!isEnabled()) {
      return
    }

    val block = event.getClickedBlock()
    if ((mimicIdentifier.isMimic(block)) && (event.getAction() == Action.RIGHT_CLICK_BLOCK)) {
      KillPlayerRunnable(event.player).runTaskLater(plugin, KILL_DELAY.toLong())
    }

  }

  @EventHandler
  fun onInventoryClick(event: InventoryClickEvent) {
    if (!isEnabled()) {
      return
    }

    val inventory = event.clickedInventory
    if ((inventory != null) && (mimicIdentifier.belongsToMimic(inventory))) {
      event.setCancelled(true)
    }
  }

}
