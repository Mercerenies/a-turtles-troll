
package com.mercerenies.turtletroll.ext

import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.enchantments.Enchantment

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.ComponentBuilder
import net.kyori.adventure.text.BuildableComponent

import kotlin.collections.Map
import kotlin.collections.HashMap

// TODO This entire file is deprecated. Move all helpers into
// appropriate classes or into util package.

fun<T> MutableIterator<T>.retainAll(func: (T) -> Boolean): List<T> {
  val result = ArrayList<T>()
  while (this.hasNext()) {
    val curr = this.next()
    if (!func(curr)) {
      result.add(curr)
      this.remove()
    }
  }
  return result
}

fun BlockBreakEvent.getDefaultDrops(): Collection<ItemStack> =
  block.getDrops(player.inventory.itemInMainHand, player)

val<T> List<T>.tail: List<T>?
  get() =
    if (this.size == 0) {
      null
    } else {
      this.subList(1, this.size)
    }

val<T> List<T>.head: T?
  get() =
    if (this.size == 0) {
      null
    } else {
      this[0]
    }

// Left-biased union
infix fun<K, V> Map<K, V>.union(that: Map<K, V>): Map<K, V> {
  val merged = HashMap(that)
  merged.putAll(this)
  return merged
}

fun Component.append(text: String): Component =
  this.append(Component.text(text))

fun<C : BuildableComponent<C, B>, B : ComponentBuilder<C, B>> ComponentBuilder<C, B>.append(text: String): B =
  this.append(Component.text(text))

// ItemStack.addEnchantment, but fluent
fun ItemStack.withEnchantment(enchantment: Enchantment, level: Int): ItemStack {
  this.addEnchantment(enchantment, level)
  return this
}
