
package com.mercerenies.turtletroll.demand

interface GodsState : GodsConditionAccessor {

  fun setGodsAppeased(isAppeased: Boolean): Unit

  // As setGodsAppeased, but do not notify players that the state has
  // been changed.
  fun setGodsAppeasedSilently(isAppeased: Boolean): Unit

}
