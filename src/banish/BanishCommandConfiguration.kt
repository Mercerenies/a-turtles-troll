
package com.mercerenies.turtletroll.banish

import com.mercerenies.turtletroll.feature.HasEnabledStatus

import org.bukkit.World

interface BanishCommandConfiguration : HasEnabledStatus {
  val superflatWorld: World

  companion object {
    val DISABLED_MESSAGE = "The banishment feature is currently disabled on this feature"
    val PLAYER_ONLY_MESSAGE = "The banish command can only be used by players"
  }

}
