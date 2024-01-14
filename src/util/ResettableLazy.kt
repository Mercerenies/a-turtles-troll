
package com.mercerenies.turtletroll.util

// Like the built-in `lazy` Kotlin function, but provides a reset()
// function that un-initializes the object.
class ResettableLazy<out T>(
  private val initializer: () -> T,
) : Lazy<T> {

  private val lock: Any = Any()

  // Invariant: If `initialized` is true and T is non-nullable then
  // `_value` is non-null. All modifications to either of these
  // variables takes place while synchronized on `this.lock`.
  private var initialized: Boolean = false
  private var _value: T? = null

  override val value: T
    get() =
      synchronized(lock) {
        if (initialized) {
          // Cast safety: See invariant comment above.
          @Suppress("UNCHECKED_CAST")
          _value as T
        } else {
          val result = initializer()
          _value = result
          initialized = true
          result
        }
      }

  override fun isInitialized(): Boolean =
    synchronized(lock) {
      initialized
    }

  override fun toString(): String =
    if (isInitialized()) {
      value.toString()
    } else {
      "#<Uninitialized ResettableLazy>"
    }

  fun reset() {
    synchronized(lock) {
      initialized = false
      _value = null
    }
  }

}
