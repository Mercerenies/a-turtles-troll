
package com.mercerenies.turtletroll.demand

interface GodsState : GodsConditionAccessor {

  fun setGodsAppeased(isAppeased: Boolean): Unit

}
