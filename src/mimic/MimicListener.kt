
package com.mercerenies.turtletroll.mimic

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
import org.bukkit.scheduler.BukkitRunnable
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack

import net.kyori.adventure.text.Component

import kotlin.random.Random

class MimicListener(
  val plugin: Plugin,
  private val deathRegistry: CustomDeathMessageRegistry,
  private val mobReplaceChance: Double,
  private val contentsFactory: MimicContentsFactory,
) : AbstractFeature(), Listener {

  companion object {
    val KILL_DELAY = Constants.TICKS_PER_SECOND

    val SAFETY_RADIUS = 8

    val MOBS_TO_REPLACE = setOf(
      EntityType.ZOMBIE, EntityType.SKELETON, EntityType.SPIDER, EntityType.ZOMBIFIED_PIGLIN,
      EntityType.STRAY, EntityType.HUSK,
    )

    // Returns an array of the current contents of the inventory,
    // which will not change even if the inventory's contents are
    // later changed.
    fun takeSnapshot(inventory: Inventory): Array<ItemStack?> {
      val contents: Array<ItemStack?> = inventory.contents!!.copyOf()
      for (i in contents.indices) {
        contents[i] = contents[i]?.clone()
      }
      return contents
    }

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

  private val contentsStore = ChestContentsMemoryStore()

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
      // Plan to kill the player.
      KillPlayerRunnable(event.player).runTaskLater(plugin, KILL_DELAY.toLong())
      // Replace the inventory contents with something from our
      // factory.
      event.setCancelled(true)
      val inventoryHolder = MimicInventoryHolder(contentsStore, contentsFactory)
      event.player.openInventory(inventoryHolder.getInventory())
    }

  }

  @EventHandler
  fun onInventoryOpen(event: InventoryOpenEvent) {
    if (!isEnabled()) {
      return
    }
    val player = event.player
    val inventory = event.inventory

    if (player !is Player) {
      return // An NPC opened an inventory(?). Ignore it.
    }
    if (inventory.getType() != InventoryType.CHEST) {
      return // Ignore non-chests
    }

    // Take an immutable snapshot and remember the contents for later
    // mimics.
    val contents = takeSnapshot(inventory)
    contentsStore.setContents(player, contents)
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
