
package com.mercerenies.turtletroll.cookie

import com.mercerenies.turtletroll.ext.*
import com.mercerenies.turtletroll.Messages

import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import org.bukkit.entity.Player
import org.bukkit.plugin.Plugin
import org.bukkit.scheduler.BukkitRunnable

abstract class FillInventorySlotsEffect(private val plugin: Plugin) : CookieEffect {

  companion object {
    private val DELAY = 2
    private val PLAYER_INVENTORY_SIZE = 41
  }

  class Constant(
    _plugin: Plugin,
    override val message: String,
    val newStack: ItemStack,
    val replacesAll: Boolean = false,
  ) : FillInventorySlotsEffect(_plugin) {

    override fun getReplacement(itemStack: ItemStack): ItemStack? =
      if ((itemStack.type == Material.AIR) || (replacesAll)) {
        newStack
      } else {
        null
      }

  }

  private inner class FillRunnable(val player: Player) : BukkitRunnable() {
    override fun run() {
      val inv = player.inventory
      for (i in 0 until PLAYER_INVENTORY_SIZE) {
        val existingItem = inv.getItem(i)
        val replacement = getReplacement(existingItem ?: ItemStack(Material.AIR, 0))
        if (replacement != null) {
          inv.setItem(i, replacement)
        }
      }
    }
  }

  abstract val message: String

  // Return null to decline to replace
  abstract fun getReplacement(itemStack: ItemStack): ItemStack?

  open override fun cancelsDefault(): Boolean = false

  override fun onEat(action: CookieEatenAction) {
    val player = action.player
    Messages.sendMessage(player, message)
    FillRunnable(player).runTaskLater(plugin, DELAY.toLong())
  }

}
