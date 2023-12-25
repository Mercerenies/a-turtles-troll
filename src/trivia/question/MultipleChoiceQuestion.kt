
package com.mercerenies.turtletroll.trivia.question

import com.mercerenies.turtletroll.ext.*
import com.mercerenies.turtletroll.Messages

import net.kyori.adventure.text.Component

class MultipleChoiceQuestion(
  private val questionBody: String,
  answers: List<String>,
  correctAnswerIndex: Int,
  private val rewards: List<TriviaQuestionReward>,
  shuffleAnswers: Boolean = true,
) : TriviaQuestion {

  companion object {

    val LETTERS = listOf(
      'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M',
      'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z',
    )

    fun letterToIndex(letter: Char): Int? =
      when {
        letter.isLowerCase() -> letterToIndex(letter.uppercaseChar())
        (letter >= 'A') && (letter <= 'Z') -> (letter - 'A').toInt()
        else -> null
      }

  }

  init {
    if (answers.size > 26) {
      throw IllegalArgumentException("Answer lists of length > 26 are not currently supported")
    }
  }

  private val answersList: List<String> =
    if (shuffleAnswers) {
      val newList = ArrayList(answers)
      newList.shuffle()
      newList
    } else {
      answers
    }

  private val correctAnswerBody: String = answers[correctAnswerIndex]

  override val canonicalAnswer: String = run {
    val index = answersList.indexOf(correctAnswerBody)
    if (index < 0) {
      throw IllegalArgumentException("Answer ${correctAnswerBody} is not in answers list ${answersList}")
    }
    val letter = LETTERS[index]
    "$letter. $correctAnswerBody"
  }

  override fun askQuestion() {
    Messages.broadcastMessage(questionBody)
    for ((letter, answer) in LETTERS.zip(answersList)) {
      Messages.broadcastMessage(Component.text(letter).append(". ").append(answer))
    }
  }

  override fun acceptAnswer(answer: String): Boolean {
    if (answer.length != 1) {
      return false
    }
    val index = letterToIndex(answer.first())
    return (index != null) && (index < answersList.size)
  }

  override fun checkAnswer(answer: String): Boolean {
    val index = letterToIndex(answer.first())!!
    val actualAnswer = answersList[index]
    return (actualAnswer == correctAnswerBody)
  }

  override fun chooseReward(): TriviaQuestionReward =
    rewards.random()

}
