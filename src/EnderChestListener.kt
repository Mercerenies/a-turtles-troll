
package com.mercerenies.turtletroll

import com.mercerenies.turtletroll.feature.AbstractFeature
import com.mercerenies.turtletroll.feature.container.FeatureContainer
import com.mercerenies.turtletroll.feature.container.ListenerContainer
import com.mercerenies.turtletroll.feature.builder.BuilderState
import com.mercerenies.turtletroll.feature.builder.FeatureContainerFactory

import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.PlayerDeathEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.event.block.Action
import org.bukkit.entity.Player
import org.bukkit.block.Block
import org.bukkit.Material

class EnderChestListener() : AbstractFeature(), Listener {

  companion object : FeatureContainerFactory<FeatureContainer> {

    private fun isEnderChest(block: Block): Boolean =
      block.type == Material.ENDER_CHEST

    override fun create(state: BuilderState): FeatureContainer =
      ListenerContainer(EnderChestListener())

  }

  private var mostRecentDeath: Player? = null

  override val name = "enderchest"

  override val description = "When a player opens an ender chest, they see the ender chest inventory of whoever died most recently"

  @EventHandler
  fun onPlayerInteract(event: PlayerInteractEvent) {
    if (!isEnabled()) {
      return
    }

    val block = event.getClickedBlock()
    if ((block != null) && (isEnderChest(block)) && (event.getAction() == Action.RIGHT_CLICK_BLOCK)) {
      val targetPlayer = mostRecentDeath
      if (targetPlayer != null) {
        event.setCancelled(true)
        event.player.openInventory(targetPlayer.enderChest)
      }
    }

  }

  @EventHandler
  fun onPlayerDeath(event: PlayerDeathEvent) {
    // Record the death but do not interfere. This happens even if the
    // feature is disabled.
    mostRecentDeath = event.entity
  }

}
