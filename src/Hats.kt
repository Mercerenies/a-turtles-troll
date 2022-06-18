
package com.mercerenies.turtletroll

import com.mercerenies.turtletroll.ext.*

import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import org.bukkit.entity.Player

object Hats {

  private val customHatNames: Set<String> =
    setOf(
      "arrow hat", "cauldron hat", "chest hat", "dripstone hat", "hat hat",
      "minecart hat", "shulker hat", "witch hat",
    )

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
    return (meta.hasDisplayName()) && (isCustomHat(meta.getDisplayName()))
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
    val name = customHatNames.toList().sample()
    val meta = stack.itemMeta
    if (meta != null) {
      meta.setDisplayName(name)
      stack.itemMeta = meta
    }
    return stack
  }

}
