
package com.mercerenies.turtletroll.gravestone

interface CustomDeathMessageRegistry {

  // Unit implementation of CustomDeathMessageRegistry which just
  // calls the block once.
  object Unit : CustomDeathMessageRegistry {
    override fun<R> withCustomDeathMessage(message: CustomDeathMessage, block: () -> R): R {
      return block()
    }
  }

  fun<R> withCustomDeathMessage(message: CustomDeathMessage, block: () -> R): R

}
