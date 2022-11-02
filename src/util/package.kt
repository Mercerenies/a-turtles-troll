
package com.mercerenies.turtletroll.util

import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.ItemMeta

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

fun ItemStack.withItemMeta(f: (ItemMeta?) -> Unit): ItemStack {
  // The ItemMeta is always copied when we get it from ItemStack, so
  // we have to take a copy, modify the copy, and then put it back.
  val meta = this.itemMeta
  f(meta)
  this.itemMeta = meta
  return this
}
