
package com.mercerenies.turtletroll.trivia.question

import com.mercerenies.turtletroll.Messages

class NumericalQuestion(
  private val questionBody: String,
  private val correctAnswer: Int,
  private val rewards: List<TriviaQuestionReward>,
) : TriviaQuestion {

  override val canonicalAnswer: String
    get() = correctAnswer.toString()

  override fun askQuestion() {
    Messages.broadcastMessage(questionBody)
  }

  override fun acceptAnswer(answer: String): AnswerResult {
    if (answer.toIntOrNull() == null) {
      return AnswerResult.ErrorResult("Please enter a valid integer.")
    }
    return AnswerResult.SuccessResult
  }

  override fun checkAnswer(answer: String): Boolean =
    answer.toInt() == correctAnswer

  override fun chooseReward(): TriviaQuestionReward =
    rewards.random()

}
