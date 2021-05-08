
package com.mercerenies.turtletroll.ext

import org.bukkit.Location

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
