
package com.mercerenies.turtletroll.nms

interface EntityMetadata {
  val id: Int
  val serializer: Any?
  var value: Any?

  fun getHandle(): Any

}
