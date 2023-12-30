
package com.mercerenies.turtletroll.feature

// The bare minimum API required for a feature to be considered
// "runnable" by the Turtle Troll API. Most implementors of this
// interface will want to subclass either TurtleRunnable or
// RunnableFeature, as the two most common use cases for this
// interface. (TODO Deprecate and remove; it's a GameModification now)
interface Schedulable {
  fun register(): Unit
  fun cancel(): Unit
}
