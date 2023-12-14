
package com.mercerenies.turtletroll.banish

import com.mercerenies.turtletroll.feature.HasEnabledStatus

import org.bukkit.World

interface BanishCommandConfiguration : HasEnabledStatus {
  val superflatWorld: World

  companion object {
    val DISABLED_MESSAGE = "The banishment feature is currently disabled on this server"
    val PLAYER_ONLY_MESSAGE = "The banish command can only be used by players"
    val BANISHMENT_MESSAGE = "For your crimes against Minecraft, you have been banished to a superflat world!"
  }

}
