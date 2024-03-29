
package com.mercerenies.turtletroll.trivia

import com.mercerenies.turtletroll.trivia.question.TriviaQuestion
import com.mercerenies.turtletroll.trivia.question.TriviaQuestionSupplier
import com.mercerenies.turtletroll.trivia.question.TriviaQuestionReward
import com.mercerenies.turtletroll.trivia.question.ItemReward
import com.mercerenies.turtletroll.trivia.question.AnswerResult
import com.mercerenies.turtletroll.util.component.*
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

  fun hasQuestion(): Boolean =
    currentQuestion != null

  fun askNewRandomQuestion() {
    val newQuestion = questionSupplier.supply()
    askQuestion(newQuestion)
  }

  fun askQuestion(questionId: String) {
    val newQuestion = questionSupplier.supply(questionId)
    askQuestion(newQuestion)
  }

  fun askQuestion(question: TriviaQuestion) {
    Messages.broadcastMessage("*** TRIVIA TIME! ***")
    question.askQuestion()
    currentQuestion = question
    answers.clear()
    Messages.broadcastMessage("Use `/turtle answer <your_answer>` to answer the question!")
  }

  fun acceptAnswer(player: Player, answer: String): AnswerResult {
    val question = currentQuestion
    if (question == null) {
      return AnswerResult.ErrorResult("There is no active trivia question at this time.")
    }
    val acceptance = question.acceptAnswer(answer)
    if (acceptance.isSuccessful()) {
      answers[player.uniqueId] = answer
    }
    return acceptance
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
    currentQuestion = null // Clear the current question field
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
