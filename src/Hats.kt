
package com.mercerenies.turtletroll

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
      "umbrella hat",
    )

  private val customHatNamesList = customHatNames.toList()

  val UMBRELLA_HAT_NAME = "umbrella hat"

  fun isCustomHat(hatName: String): Boolean =
    customHatNames.contains(hatName)

  fun isCustomHat(stack: ItemStack): Boolean =
    getCustomHatName(stack) != null

  fun getCustomHatName(stack: ItemStack): String? {
    if (stack.getType() != Material.CARVED_PUMPKIN) {
      return null
    }

    val meta = stack.itemMeta
    if ((meta == null) || (!meta.hasDisplayName())) {
      return null
    }

    val hatName = meta.displayName()!!.asPlainText()
    if (isCustomHat(hatName)) {
      return hatName
    } else {
      return null
    }
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
    val name = customHatNamesList.random()
    val meta = stack.itemMeta
    if (meta != null) {
      meta.displayName(Component.text(name))
      stack.itemMeta = meta
    }
    return stack
  }

}
