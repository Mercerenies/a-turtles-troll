
package com.mercerenies.turtletroll.ext

import org.bukkit.Location
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.inventory.ItemStack

import kotlin.random.Random
import kotlin.collections.Map
import kotlin.collections.HashMap

fun Location.nearby(distance: Int): List<Location> {
  val result = ArrayList<Location>()
  for (z in -distance..distance) {
    for (y in -distance..distance) {
      for (x in -distance..distance) {
        if (Math.abs(x) + Math.abs(y) + Math.abs(z) <= distance) {
          result.add(Location(this.world, x.toDouble(), y.toDouble(), z.toDouble()).add(this))
        }
      }
    }
  }
  return result
}

fun Location.nearbyXZ(distance: Int): List<Location> {
  val result = ArrayList<Location>()
  for (z in -distance..distance) {
    for (x in -distance..distance) {
      if (Math.abs(x) + Math.abs(z) <= distance) {
        result.add(Location(this.world, x.toDouble(), 0.0, z.toDouble()).add(this))
      }
    }
  }
  return result
}

fun Location.isExposedToSky(): Boolean =
  this.getBlock().getLightFromSky() == 15.toByte()

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

fun<T> List<T>.sample(): T? =
  if (this.size == 0) {
    null
  } else {
    this[Random.nextInt(this.size)]
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
