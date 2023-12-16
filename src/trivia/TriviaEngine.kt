
package com.mercerenies.turtletroll.trivia

import com.mercerenies.turtletroll.trivia.question.TriviaQuestion
import com.mercerenies.turtletroll.trivia.question.TriviaQuestionSupplier
import com.mercerenies.turtletroll.trivia.question.TriviaQuestionReward
import com.mercerenies.turtletroll.trivia.question.ItemReward
import com.mercerenies.turtletroll.ext.*
import com.mercerenies.turtletroll.Messages
import com.mercerenies.turtletroll.location.PlayerSelector

import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.Material

import net.kyori.adventure.text.Component

import kotlin.collections.HashMap

import java.util.UUID

class TriviaEngine(
  private val questionSupplier: TriviaQuestionSupplier,
) {

  companion object {

    private val TRIVIAL_REWARD: TriviaQuestionReward =
      ItemReward(ItemStack(Material.DIRT, 1))

  }

  private var currentQuestion: TriviaQuestion? = null
  private val answers: HashMap<UUID, String> = HashMap()

  fun askNewQuestion() {
    Messages.broadcastMessage("*** TRIVIA TIME! ***")
    val newQuestion = questionSupplier.supply()
    newQuestion.askQuestion()
    currentQuestion = newQuestion
    answers.clear()
    Messages.broadcastMessage("Use `/turtle answer <your_answer>` to answer the question!")
  }

  fun acceptAnswer(player: Player, answer: String): Boolean {
    val question = currentQuestion
    if ((question == null) || (!question.acceptAnswer(answer))) {
      return false
    }
    answers[player.uniqueId] = answer
    return true
  }

  fun judgeAnswers(): TriviaResult {
    val question = currentQuestion
    if (question == null) {
      // No question, so no one answered
      return TriviaResult.EMPTY
    }
    Messages.broadcastMessage(Component.text("End of trivia! The correct answer was ").append(question.canonicalAnswer))
    val playerMap = PlayerSelector.makePlayerMap()
    val correctAnswerers = ArrayList<Player>()
    val incorrectAnswerers = ArrayList<Player>()
    for ((playerUuid, answer) in answers) {
      val player = playerMap[playerUuid]
      if (player == null) {
        // Player is not online anymore; they probably logged off
        continue
      }
      if (question.checkAnswer(answer)) {
        correctAnswerers.add(player)
      } else {
        incorrectAnswerers.add(player)
      }
    }
    return TriviaResult(
      correctAnswerers = correctAnswerers,
      incorrectAnswerers = incorrectAnswerers,
    )
  }

  fun chooseReward(): TriviaQuestionReward {
    val question = currentQuestion
    if (question == null) {
      return TRIVIAL_REWARD
    } else {
      return question.chooseReward()
    }
  }

}
