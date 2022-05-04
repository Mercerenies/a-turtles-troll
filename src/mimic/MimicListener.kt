
package com.mercerenies.turtletroll.mimic

import com.mercerenies.turtletroll.gravestone.CustomDeathMessageRegistry
import com.mercerenies.turtletroll.gravestone.CustomDeathMessage
import com.mercerenies.turtletroll.gravestone.Mimic
import com.mercerenies.turtletroll.feature.AbstractFeature
import com.mercerenies.turtletroll.feature.container.FeatureContainer
import com.mercerenies.turtletroll.feature.container.ListenerContainer
import com.mercerenies.turtletroll.feature.builder.BuilderState
import com.mercerenies.turtletroll.feature.builder.FeatureContainerFactory
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
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.event.entity.PlayerDeathEvent
import org.bukkit.event.entity.CreatureSpawnEvent
import org.bukkit.scheduler.BukkitRunnable

import kotlin.random.Random

class MimicListener(
  val plugin: Plugin,
  private val deathRegistry: CustomDeathMessageRegistry,
) : AbstractFeature(), Listener {

  companion object {
    val KILL_DELAY = Constants.TICKS_PER_SECOND

    val SAFETY_RADIUS = 8

    val MOB_REPLACE_CHANCE = 0.05
    val MOBS_TO_REPLACE = setOf(
      EntityType.ZOMBIE, EntityType.SKELETON, EntityType.SPIDER, EntityType.ZOMBIFIED_PIGLIN,
      EntityType.STRAY, EntityType.HUSK,
    )

  }

  private inner class KillPlayerRunnable(val player: Player) : BukkitRunnable() {

    val message = CustomDeathMessage(
      Mimic,
      "${player.getDisplayName()} was eaten by a Mimic.",
    )

    override fun run() {
      deathRegistry.withCustomDeathMessage(message) {
        player.damage(9999.0, null)
      }
    }

  }

  override val name = "mimics"

  override val description = "Chests randomly spawn in the wild which, if opened, kill you"

  @EventHandler
  fun onCreatureSpawn(event: CreatureSpawnEvent) {
    if (!isEnabled()) {
      return
    }
    if (!SpawnReason.isNatural(event)) {
      return
    }
    if (MOBS_TO_REPLACE.contains(event.entity.type)) {
      if (BlockSelector.countNearbyMatching(event.location.block, SAFETY_RADIUS, BlockSelector::isMimicOrCake) < 1) {
        if (Random.nextDouble() < MOB_REPLACE_CHANCE) {
          val below = event.location.clone().add(0.0, -1.0, 0.0)
          if (below.block.type != Material.AIR) {
            event.setCancelled(true)
            MimicIdentifier.spawnMimic(event.location.block)
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
    if ((MimicIdentifier.isMimic(block)) && (event.getAction() == Action.RIGHT_CLICK_BLOCK)) {
      KillPlayerRunnable(event.player).runTaskLater(plugin, KILL_DELAY.toLong())
    }

  }

}
