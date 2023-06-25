
package com.mercerenies.turtletroll.trivia.question

import org.bukkit.inventory.ItemStack
import org.bukkit.Material

import net.kyori.adventure.text.Component

// Identical to ItemReward but has the word "Enchanted" put in front
// of the name. It's purely flavortext; this does not add any
// enchantments to the item on its own.
class EnchantedItemReward(
  itemStack: ItemStack
) : ItemReward(itemStack) {

  constructor(material: Material) : this(ItemStack(material))

  override val name: Component
    get() = Component.text("Enchanted ").append(super.name)

}
