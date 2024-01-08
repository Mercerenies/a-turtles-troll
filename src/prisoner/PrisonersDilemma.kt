
package com.mercerenies.turtletroll.prisoner

import com.mercerenies.turtletroll.Messages
import com.mercerenies.turtletroll.util.component.*

import org.bukkit.entity.Player
import org.bukkit.Bukkit

import net.kyori.adventure.text.Component

import java.util.UUID

class PrisonersDilemma(
  private val firstTarget: UUID,
  private val firstTargetDisplayName: Component,
  private val secondTarget: UUID,
  private val secondTargetDisplayName: Component,
  private val reward: PrisonerReward,
) {

  companion object {

    private fun punishPlayer(uuid: UUID) =
      Bukkit.getPlayer(uuid)?.let { punishPlayer(it) }

    private fun punishPlayer(player: Player) {
      player.world.strikeLightning(player.location)
    }

  }

  private var firstTargetResponse = PlayerResponse.NONE
  private var secondTargetResponse = PlayerResponse.NONE

  constructor(firstTarget: Player, secondTarget: Player, reward: PrisonerReward) : this(
    firstTarget.uniqueId,
    firstTarget.displayName(),
    secondTarget.uniqueId,
    secondTarget.displayName(),
    reward,
  )

  constructor(victims: PrisonersDilemmaManager.Victims, reward: PrisonerReward) : this(
    victims.player1,
    victims.player2,
    reward,
  )

  fun notifyAllPlayers() {
    Messages.broadcastMessage("*** PRISONER'S DILEMMA! ***")
    Messages.broadcastMessage(Component.text("").append(reward.fullRewardName).append(" are on the line!"))
    Messages.broadcastMessage(
      Component.text("").append(firstTargetDisplayName).append(" and ")
        .append(secondTargetDisplayName).append(" must choose whether to SPLIT or STEAL the reward!")
    )
    Messages.broadcastMessage("But if both players choose to STEAL, then nobody gets anything!")
    Messages.broadcastMessage("Use `/turtle split` or `/turtle steal` to make your choice!")
  }

  fun acceptResponse(player: Player, response: PlayerResponse): Boolean {
    if (player.uniqueId == firstTarget) {
      firstTargetResponse = response
      return true
    } else if (player.uniqueId == secondTarget) {
      secondTargetResponse = response
      return true
    } else {
      return false
    }
  }

  fun judgeResponses() {
    if (!checkBothPlayersResponded()) {
      return
    }
    // Both players responded, so determine the outcome.
    if (firstTargetResponse == PlayerResponse.STEAL && secondTargetResponse == PlayerResponse.STEAL) {
      doDoubleStealOutcome()
    } else if (firstTargetResponse == PlayerResponse.STEAL) {
      doStealOutcome(firstTarget, firstTargetDisplayName)
    } else if (secondTargetResponse == PlayerResponse.STEAL) {
      doStealOutcome(secondTarget, secondTargetDisplayName)
    } else {
      doSplitOutcome()
    }
  }

  private fun doDoubleStealOutcome() {
    // No rewards :)
    Messages.broadcastMessage("Both players chose to STEAL, so nobody gets anything!")
  }

  private fun doStealOutcome(uuid: UUID, displayName: Component) {
    Messages.broadcastMessage(Component.text("").append(displayName).append(" chose to STEAL!"))
    Messages.broadcastMessage(Component.text("They get the full reward of ").append(reward.fullRewardName))
    Bukkit.getPlayer(uuid)?.let { reward.awardFull(it) }
  }

  private fun doSplitOutcome() {
    Messages.broadcastMessage(
      Component.text("Both players chose to SPLIT! Both players get ").append(reward.halfRewardName)
    )
    Bukkit.getPlayer(firstTarget)?.let { reward.awardHalf(it) }
    Bukkit.getPlayer(secondTarget)?.let { reward.awardHalf(it) }
  }

  // If either player did not respond, punish them and return false.
  // Otherwise, return true.
  private fun checkBothPlayersResponded(): Boolean {
    if (firstTargetResponse == PlayerResponse.NONE && secondTargetResponse == PlayerResponse.NONE) {
      Messages.broadcastMessage("Nobody responded to the prisoner's dilemma!")
      Messages.broadcastMessage(
        Component.text("Nobody gets a reward, and ").append(firstTargetDisplayName).append(" and ")
          .append(secondTargetDisplayName).append(" are both punished!")
      )
      punishPlayer(firstTarget)
      punishPlayer(secondTarget)
      return false
    } else if (firstTargetResponse == PlayerResponse.NONE) {
      Messages.broadcastMessage(
        Component.text("").append(firstTargetDisplayName).append(" did not respond to the prisoner's dilemma!")
      )
      Messages.broadcastMessage(
        Component.text("Nobody gets a reward, and ").append(firstTargetDisplayName)
          .append(" shall be punished!")
      )
      punishPlayer(firstTarget)
      return false
    } else if (secondTargetResponse == PlayerResponse.NONE) {
      Messages.broadcastMessage(
        Component.text("").append(secondTargetDisplayName).append(" did not respond to the prisoner's dilemma!")
      )
      Messages.broadcastMessage(
        Component.text("Nobody gets a reward, and ").append(secondTargetDisplayName)
          .append(" shall be punished!")
      )
      punishPlayer(secondTarget)
      return false
    } else {
      return true
    }
  }

}
