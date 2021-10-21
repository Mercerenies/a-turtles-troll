
package com.mercerenies.turtletroll.gravestone

interface DeathCondition {

  // Should be a prepositional phrase
  val description: String

  fun test(cause: CauseOfDeath): Boolean

  object True : DeathCondition {
    override val description: String = "for any reason"

    override fun test(cause: CauseOfDeath): Boolean = true

  }

}
