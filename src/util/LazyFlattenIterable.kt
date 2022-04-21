
package com.mercerenies.turtletroll.util

class LazyFlattenIterable<out T>(
  private val innerData: Iterable<Iterable<T>>,
) : Iterable<T> {

  private class LazyFlattenIterator<out T>(
    private val iterator: Iterator<Iterable<T>>,
  ) : Iterator<T> {

    private var current: Iterator<T>? = null

    override operator fun hasNext(): Boolean {
      // `current` is only null if we haven't started iteration yet.
      // As long as `current` is either null or lacks a next, keep
      // iterating over the outer list.
      var current = this.current // Pin current value locally
      while ((current == null) || (!current.hasNext())) {
        if (iterator.hasNext()) {
          current = iterator.next().iterator()
        } else {
          // If the outer iterator is exhausted, then full stop.
          this.current = current
          return false
        }
      }
      // If current != null *and* current has a next value, then our
      // flattened iterator also has a next value.
      this.current = current
      return true
    }

    override operator fun next(): T {
      // Force iteration of the outer list until we hit something that
      // has a value.
      hasNext()
      val current = this.current // Pin current value
      if (current == null) {
        // This happens if hasNext() returned false.
        throw NoSuchElementException("LazyFlattenIterable iterator exhausted")
      }
      return current.next()
    }

  }

  override fun iterator(): Iterator<T> =
    LazyFlattenIterator(innerData.iterator())

}

fun<T> Iterable<Iterable<T>>.lazyFlatten(): Iterable<T> =
  LazyFlattenIterable(this)
