
package com.mercerenies.turtletroll.nms

interface EntityMetadata<T> {
  val id: Int
  var value: T

  fun getHandle(): Any
}
