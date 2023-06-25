
package com.mercerenies.turtletroll.trivia

import com.mercerenies.turtletroll.trivia.question.TriviaQuestionReward
import com.mercerenies.turtletroll.Messages
import com.mercerenies.turtletroll.ext.*
import com.mercerenies.turtletroll.util.*

import org.bukkit.entity.Player
import org.bukkit.Bukkit

import net.kyori.adventure.text.Component

data class TriviaResult(
  val correctAnswerers: List<Player>,
  val incorrectAnswerers: List<Player>,
) {

  companion object {
    val EMPTY: TriviaResult = TriviaResult(listOf(), listOf())

    private fun correctLine(reward: TriviaQuestionReward, players: List<Player>): Component =
      Component.text("The following players received ")
        .append(reward.name)
        .append(": ")
        .append(pluralize(players.map { it.displayName() }))

    private fun incorrectLine(players: List<Player>): Component =
      Component.text("The following players guessed incorrectly: ")
        .append(pluralize(players.map { it.displayName() }))

    private fun abstainLine(players: List<Player>): Component =
      Component.text("The following players failed to answer and should be shamed publicly: ")
        .append(pluralize(players.map { it.displayName() }))

    fun assignRewards(result: TriviaResult, reward: TriviaQuestionReward) {
      val punishedPlayers = ArrayList<Player>()
      for (player in Bukkit.getOnlinePlayers()) {
        when (result.classify(player)) {
          Classification.ABSTAINED -> {
            punishedPlayers.add(player)
            player.world.strikeLightning(player.location)
          }
          Classification.INCORRECT -> {
            // No action
          }
          Classification.CORRECT -> {
            reward.rewardPlayer(player)
          }
        }
      }
      Messages.broadcastMessage(correctLine(reward, result.correctAnswerers))
      Messages.broadcastMessage(incorrectLine(result.incorrectAnswerers))
      Messages.broadcastMessage(abstainLine(punishedPlayers))
    }

  }

  enum class Classification {
    ABSTAINED,
    INCORRECT,
    CORRECT,
  }

  fun classify(player: Player): Classification =
    when {
      correctAnswerers.contains(player) -> Classification.CORRECT
      incorrectAnswerers.contains(player) -> Classification.INCORRECT
      else -> Classification.ABSTAINED
    }

}
