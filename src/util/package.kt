
package com.mercerenies.turtletroll.util

import com.mercerenies.turtletroll.ext.*

import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.ItemMeta
import org.bukkit.World
import org.bukkit.GameRule

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer

import java.util.UUID

import kotlin.math.min
import kotlin.math.max

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

fun pluralize(items: List<Component>, conjunction: String = "and"): Component =
  when (items.size) {
    0 -> Component.text("")
    1 -> items[0]
    2 -> items[0].append(" ${conjunction} ").append(items[1])
    else -> {
      val itemCount = items.size
      val result = Component.text()
      for ((i, s) in items.withIndex()) {
        if (i == 0) {
          result.append(s)
        } else if (i == itemCount - 1) {
          result.append(", ${conjunction} ").append(s)
        } else {
          result.append(", ").append(s)
        }
      }
      result.build()
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

fun Component.asPlainText(): String =
  PlainTextComponentSerializer.plainText().serialize(this)

fun joinWithCommas(components: List<Component>): Component {
  var first = true
  val builder = Component.text()
  for (component in components) {
    if (first) {
      first = false
    } else {
      builder.append(", ")
    }
    builder.append(component)
  }
  return builder.build()
}

fun<T> World.getGameRuleValueOrDefault(rule: GameRule<T>): T =
  getGameRuleValue(rule) ?: getGameRuleDefault(rule)!!

inline fun<T, R> T?.andThen(function: (T) -> R?): R? =
  if (this == null) {
    null
  } else {
    function(this)
  }
