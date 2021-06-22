
package com.mercerenies.turtletroll.egg

import org.bukkit.Location
import org.bukkit.entity.Item
import org.bukkit.inventory.ItemStack

import kotlin.reflect.KClass
import kotlin.random.Random

fun SpawnItemEffect(item: ItemStack): SpawnEntityEffect<Item> =
  SpawnEntityEffect(Item::class).andThen { it.itemStack = item }
