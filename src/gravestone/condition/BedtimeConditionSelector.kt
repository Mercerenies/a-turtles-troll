
package com.mercerenies.turtletroll.gravestone.condition

import com.mercerenies.turtletroll.gravestone.DeathCondition

interface BedtimeConditionSelector {
  fun chooseCondition(): DeathCondition
}
