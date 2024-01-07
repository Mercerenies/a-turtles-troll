
package com.mercerenies.turtletroll.util

import com.mercerenies.turtletroll.util.component.*

import org.bukkit.plugin.Plugin
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.ItemMeta
import org.bukkit.World
import org.bukkit.GameRule
import org.bukkit.enchantments.Enchantment
import org.bukkit.scheduler.BukkitRunnable
import org.bukkit.scheduler.BukkitScheduler

import net.kyori.adventure.text.Component

import kotlin.math.min
import kotlin.math.max

import java.time.Duration

fun lerp(a: Double, b: Double, amount: Double): Double =
  a * (1 - amount) + b * amount

fun inverseLerp(a: Double, b: Double, value: Double): Double =
  if (a == b) {
    a
  } else {
    (value - a) / (b - a)
  }

fun linearRescale(a0: Double, b0: Double, a1: Double, b1: Double, value: Double): Double =
  lerp(a1, b1, inverseLerp(a0, b0, value))

fun clamp(amount: Double, a: Double, b: Double): Double =
  min(max(amount, a), b)

fun clamp(amount: Int, a: Int, b: Int): Int =
  min(max(amount, a), b)

fun pluralize(items: List<String>, conjunction: String = "and"): String =
  when (items.size) {
    0 -> ""
    1 -> items[0]
    2 -> "${items[0]} ${conjunction} ${items[1]}"
    else -> {
      val itemCount = items.size
      var result = ""
      for ((i, s) in items.withIndex()) {
        if (i == 0) {
          result += s
        } else if (i == itemCount - 1) {
          result += ", ${conjunction} ${s}"
        } else {
          result += ", ${s}"
        }
      }
      result
    }
  }

fun amounts(n: Int, unit: String, suffix: String = "s") =
  if (n == 1) {
    unit
  } else {
    unit + suffix
  }

fun ItemStack.withItemMeta(f: (ItemMeta?) -> Unit): ItemStack {
  // The ItemMeta is always copied when we get it from ItemStack, so
  // we have to take a copy, modify the copy, and then put it back.
  val meta = this.itemMeta
  f(meta)
  this.itemMeta = meta
  return this
}

fun<T> World.getGameRuleValueOrDefault(rule: GameRule<T>): T =
  getGameRuleValue(rule) ?: getGameRuleDefault(rule)!!

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

// ItemStack.addEnchantment, but fluent
fun ItemStack.withEnchantment(enchantment: Enchantment, level: Int): ItemStack {
  this.addEnchantment(enchantment, level)
  return this
}

fun ItemStack.withCustomName(name: Component): ItemStack {
  val itemMeta = this.itemMeta
  itemMeta.displayName(name)
  this.itemMeta = itemMeta
  return this
}

fun ItemStack.withCustomName(name: String): ItemStack =
  this.withCustomName(Component.text(name))

// BukkitRunnable.cancel(), but don't throw if it's already cancelled.
fun BukkitRunnable.tryCancel() {
  try {
    this.cancel()
  } catch (_: IllegalStateException) {
    // Ignore.
  }
}

fun durationOfTicks(ticks: Long) =
  Duration.ofMillis(ticks * 50L)

// Arguments re-ordered to be more Kotlin-friendly.
fun BukkitScheduler.runTaskLater(plugin: Plugin, delay: Long, task: Runnable) =
  this.runTaskLater(plugin, task, delay)

// Arguments re-ordered to be more Kotlin-friendly.
fun BukkitScheduler.runTaskTimer(plugin: Plugin, delay: Long, period: Long, task: Runnable) =
  this.runTaskTimer(plugin, task, delay, period)
