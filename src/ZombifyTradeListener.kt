
package com.mercerenies.turtletroll

import com.mercerenies.turtletroll.feature.AbstractFeature
import com.mercerenies.turtletroll.feature.container.FeatureContainer
import com.mercerenies.turtletroll.feature.container.ListenerContainer
import com.mercerenies.turtletroll.feature.builder.BuilderState
import com.mercerenies.turtletroll.feature.builder.FeatureContainerFactory

import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.event.player.PlayerInteractEntityEvent
import org.bukkit.inventory.MerchantInventory
import org.bukkit.entity.Villager
import org.bukkit.entity.Player
import org.bukkit.entity.HumanEntity
import org.bukkit.entity.ZombieVillager
import org.bukkit.plugin.Plugin
import org.bukkit.scheduler.BukkitRunnable

import kotlin.collections.HashMap

class ZombifyTradeListener(val plugin: Plugin) : AbstractFeature(), Listener {

  companion object : FeatureContainerFactory<FeatureContainer> {

    val DELAY = 2L

    override fun create(state: BuilderState): FeatureContainer =
      ListenerContainer(ZombifyTradeListener(state.plugin))

  }

  private inner class DelayedZombify(
    val player: HumanEntity,
    val villager: Villager,
  ) : BukkitRunnable() {
    override fun run() {
      if (playerUI[player] != null) {
        // On the offchance that something else canceled the
        // InventoryCloseEvent, we don't want to accidentally kill the
        // villager and cause a stack overflow. So if we're still
        // looking at a merchant inventory, don't do anything.
        if (player.getOpenInventory().getTopInventory() !is MerchantInventory) {
          val zombie = villager.world.spawn(villager.location, ZombieVillager::class.java)
          zombie.villagerType = villager.villagerType
          zombie.villagerProfession = villager.profession
          villager.remove()
          playerUI.remove(player)
        }
      }
    }
  }

  private val playerUI = HashMap<Player, Villager>()

  override val name = "zombietrade"

  override val description = "Villagers turn into zombies after being traded with"

  @EventHandler
  fun onPlayerInteractEntity(event: PlayerInteractEntityEvent) {
    if (!isEnabled()) {
      return
    }
    val target = event.getRightClicked()
    if (target is Villager) {
      playerUI[event.player] = target
    }
  }

  @EventHandler
  fun onInventoryClose(event: InventoryCloseEvent) {
    if (!isEnabled()) {
      return
    }
    val inv = event.inventory
    if (inv is MerchantInventory) {
      val target = playerUI[event.player]
      if (target != null) {
        // It looks like, at some point, the order of events changed
        // so that, when this event is fired, the inventory is *still*
        // up. So if we kill the villager right at this instant, it'll
        // try to close the inventory and cause an infinite loop. So
        // put a 2-frame delay on it and it should all be good.
        DelayedZombify(event.player, target).runTaskLater(plugin, DELAY)
      }
    }
  }

}
