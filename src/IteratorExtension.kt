
package com.mercerenies.turtletroll

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
