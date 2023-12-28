
package com.mercerenies.turtletroll

import com.mercerenies.turtletroll.feature.AbstractFeature
import com.mercerenies.turtletroll.feature.container.FeatureContainer
import com.mercerenies.turtletroll.feature.container.ListenerContainer
import com.mercerenies.turtletroll.feature.builder.BuilderState
import com.mercerenies.turtletroll.feature.builder.FeatureContainerFactory
import com.mercerenies.turtletroll.location.BlockSelector
import com.mercerenies.turtletroll.mimic.MimicIdentifier

import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.event.block.Action
import org.bukkit.entity.Player
import org.bukkit.block.Block
import org.bukkit.block.Chest
import org.bukkit.plugin.Plugin
import org.bukkit.Material

import kotlin.collections.HashMap
import kotlin.random.Random

class ChestShuffleListener(
  private val plugin: Plugin,
) : AbstractFeature(), Listener {

  companion object : FeatureContainerFactory<FeatureContainer> {

    private val ATTEMPTS = 500

    // TODO Move this to config file
    private val RECENT_PLAYER_CHEST_PROBABILITY = 0.1

    override fun create(state: BuilderState): FeatureContainer =
      ListenerContainer(ChestShuffleListener(state.plugin))

  }

  override val name = "chestshuffle"

  override val description = "When you attempt to open a chest, a different chest might accidentally be opened instead"

  private val mimicIdentifier = MimicIdentifier(plugin)

  private val recentChestLocations = HashMap<Player, Block>()

  @EventHandler
  fun onPlayerInteract(event: PlayerInteractEvent) {
    if (!isEnabled()) {
      return
    }

    val block = event.getClickedBlock()
    if ((block != null) && (isChest(block)) && (event.getAction() == Action.RIGHT_CLICK_BLOCK)) {
      // We clicked a chest, try to find one to swap it with.
      val newBlock = findNearbyChest(block)
      if (newBlock != null) {
        event.setCancelled(true)
        val inv = (newBlock.getState() as Chest).getBlockInventory()
        event.player.openInventory(inv)
      } else {
        // If we don't replace it, then remember the location of this
        // chest for later.
        recentChestLocations[event.player] = block
      }
    }

  }

  private fun isChest(block: Block): Boolean {
    if (mimicIdentifier.isMimic(block)) {
      // Never consider mimics to be chests, by the definition
      // needed for this listener.
      return false
    }
    return block.getType() == Material.CHEST || block.getType() == Material.TRAPPED_CHEST
  }

  private fun findNearbyChest(block: Block): Block? {
    // Small probability to roll a recent player's chest
    if (Random.nextDouble() < RECENT_PLAYER_CHEST_PROBABILITY) {
      val recentBlock = recentChestLocations.values.randomOrNull()
      if ((recentBlock != null) && (isChest(recentBlock))) {
        return block
      }
    }

    // Roll nearby locations
    for (i in 1..ATTEMPTS) {
      val attempt = BlockSelector.getRandomBlockNearDims(block, distance = 8)
      if (isChest(attempt)) {
        return attempt
      }
    }

    // Failed to find a chest, return null
    return null
  }

}
