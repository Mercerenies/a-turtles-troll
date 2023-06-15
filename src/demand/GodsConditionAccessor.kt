
package com.mercerenies.turtletroll.demand

import com.mercerenies.turtletroll.ext.*

// Interface for things capable of asking how the gods are feeling
// right now.
interface GodsConditionAccessor {

  object AlwaysAppeased : GodsConditionAccessor {
    override fun getGodsStatus(): GodsStatus =
      GodsStatus.APPEASED
  }

  fun getGodsStatus(): GodsStatus

  fun isAngry(): Boolean =
    getGodsStatus() == GodsStatus.ANGRY

  fun isAppeased(): Boolean =
    getGodsStatus() == GodsStatus.APPEASED

}

enum class GodsStatus {
  APPEASED, ANGRY, IDLE,
}
