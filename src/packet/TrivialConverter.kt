
package com.mercerenies.turtletroll.packet

import com.comphenix.protocol.reflect.EquivalentConverter

object TrivialConverter : EquivalentConverter<Any?> {

  override fun getGeneric(specific: Any?): Any? =
    specific

  override fun getSpecific(generic: Any?): Any? =
    generic

  // We need Class<Any?> for the type. Java doesn't care about
  // nullability, so Class<Any> is good enough.
  @Suppress("UNCHECKED_CAST")
  override fun getSpecificType(): Class<Any?> =
    Any::class.java as Class<Any?>

}
