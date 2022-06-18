
package com.mercerenies.turtletroll

import org.bukkit.Material
import org.bukkit.inventory.ItemStack

object Hats {

  private val customHatNames: Set<String> =
    setOf(
      "arrow hat", "cauldron hat", "chest hat", "dripstone hat", "hat hat",
      "minecart hat", "shulker hat", "witch hat",
    )

  fun isCustomHat(hatName: String): Boolean =
    customHatNames.contains(hatName)

  fun isCustomHat(stack: ItemStack): Boolean {
    val meta = stack.itemMeta
    if (meta == null) {
      return false;
    }
    return (meta.hasDisplayName()) && (isCustomHat(meta.getDisplayName()))
  }

}
