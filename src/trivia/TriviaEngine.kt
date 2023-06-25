
package com.mercerenies.turtletroll.trivia

import com.mercerenies.turtletroll.trivia.question.TriviaQuestion
import com.mercerenies.turtletroll.trivia.question.TriviaQuestionSupplier
import com.mercerenies.turtletroll.trivia.question.TriviaQuestionReward
import com.mercerenies.turtletroll.trivia.question.ItemReward
import com.mercerenies.turtletroll.ext.*
import com.mercerenies.turtletroll.Messages

import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.Material

import net.kyori.adventure.text.Component

import kotlin.collections.HashMap

class TriviaEngine(
  private val questionSupplier: TriviaQuestionSupplier,
) {

  companion object {
    private val TRIVIAL_REWARD: TriviaQuestionReward =
      ItemReward(ItemStack(Material.DIRT, 1))
  }

  private var currentQuestion: TriviaQuestion? = null
  private val answers: HashMap<Player, String> = HashMap()

  fun askNewQuestion() {
    val newQuestion = questionSupplier.supply()
    newQuestion.askQuestion()
    currentQuestion = newQuestion
    answers.clear()
  }

  fun acceptAnswer(player: Player, answer: String): Boolean {
    val question = currentQuestion
    if ((question == null) || (!question.acceptAnswer(answer))) {
      return false
    }
    answers[player] = answer
    return true
  }

  fun judgeAnswers(): TriviaResult {
    val question = currentQuestion
    if (question == null) {
      // No question, so no one answered
      return TriviaResult.EMPTY
    }
    Messages.broadcastMessage(Component.text("End of trivia! The correct answer was ").append(question.canonicalAnswer))
    val correctAnswerers = ArrayList<Player>()
    val incorrectAnswerers = ArrayList<Player>()
    for ((player, answer) in answers) {
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
