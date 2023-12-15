
package com.mercerenies.turtletroll.demand.bowser

import com.mercerenies.turtletroll.Messages
import com.mercerenies.turtletroll.ext.*

import org.bukkit.Bukkit
import org.bukkit.entity.Player

import net.kyori.adventure.text.Component

///// Make a rewards pool
interface BowserReward {

  companion object {

    fun deliverRewardFromPool(rewardsPool: List<BowserReward>, specialPlayer: Player) {
      val reward = rewardsPool.random()!!
      Messages.broadcastMessage(reward.getMainRewardText())
      Messages.broadcastMessage(reward.getSpecialRewardText(specialPlayer))
      for (player in Bukkit.getOnlinePlayers()) {
        if (player != specialPlayer) {
          reward.giveMainReward(player)
        }
      }
      reward.giveSpecialReward(specialPlayer)
    }

  }

  // The first line of dialogue, indicating the reward everyone gets.
  fun getMainRewardText(): Component

  // The second line of dialogue, indicating the special reward for
  // whoever completed the mission. Note that the special reward
  // replaces the main reward for that player.
  fun getSpecialRewardText(player: Player): Component

  fun giveMainReward(player: Player): Unit

  fun giveSpecialReward(player: Player): Unit

}
