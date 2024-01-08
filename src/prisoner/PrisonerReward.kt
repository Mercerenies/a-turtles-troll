
package com.mercerenies.turtletroll.prisoner

import org.bukkit.entity.Player

import net.kyori.adventure.text.Component

interface PrisonerReward {

  // These names should include the quantity, so a phrase like "4
  // diamonds" is appropriate.
  val fullRewardName: Component
  val halfRewardName: Component

  fun awardFull(player: Player)
  fun awardHalf(player: Player)

}
