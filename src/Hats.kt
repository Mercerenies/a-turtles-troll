
package com.mercerenies.turtletroll

import com.mercerenies.turtletroll.ext.*
import com.mercerenies.turtletroll.util.*

import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import org.bukkit.entity.Player

import net.kyori.adventure.text.Component

object Hats {

  private val customHatNames: Set<String> =
    setOf(
      // Evanski's Hats
      "arrow hat",
      "barrel hat",
      "cauldron hat",
      "chest hat",
      "dripstone hat",
      "gangster hat",
      "hat hat",
      "minecart hat",
      "shulker hat",
      "sign hat",
      "slime hat",
      "squid hat",
      "witch hat",
    )

  private val customHatNamesList = customHatNames.toList()

  fun isCustomHat(hatName: String): Boolean =
    customHatNames.contains(hatName)

  fun isCustomHat(stack: ItemStack): Boolean {

    if (stack.getType() != Material.CARVED_PUMPKIN) {
      return false
    }

    val meta = stack.itemMeta
    if (meta == null) {
      return false
    }
    return (meta.hasDisplayName()) && (isCustomHat(meta.displayName()!!.asPlainText()))
  }

  fun isWearingOrdinaryHat(player: Player): Boolean {
    val helmet = player.inventory.helmet
    if ((helmet == null) || (helmet.getType() != Material.CARVED_PUMPKIN)) {
      // No helmet, so false
      return false
    }
    // Otherwise, check if it's a custom hat
    return !isCustomHat(helmet)
  }

  fun sampleRandomHat(): ItemStack {
    val stack = ItemStack(Material.CARVED_PUMPKIN)
    val name = customHatNamesList.sample()!!
    val meta = stack.itemMeta
    if (meta != null) {
      meta.displayName(Component.text(name))
      stack.itemMeta = meta
    }
    return stack
  }

}
