
package com.mercerenies.turtletroll.jump

import com.mercerenies.turtletroll.util.*
import com.mercerenies.turtletroll.demand.GodsConditionAccessor

import org.bukkit.entity.Player

class GodsRageStat(
  val multiplier: Double,
  private val godsCondition: GodsConditionAccessor,
) : EncumbranceStat {

  override fun calculateEncumbrance(player: Player): EncumbranceContribution {
    return EncumbranceContribution(
      amount = if (godsCondition.isAngry()) { multiplier } else { 0.0 },
      reason = "from the gods' rage",
    )
  }

}
