
package com.mercerenies.turtletroll.egg

import org.bukkit.entity.Item
import org.bukkit.inventory.ItemStack


fun SpawnItemEffect(item: ItemStack): SpawnEntityEffect<Item> =
  SpawnEntityEffect(Item::class).andThen { it.itemStack = item }
