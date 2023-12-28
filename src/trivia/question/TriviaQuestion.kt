
package com.mercerenies.turtletroll.trivia.question

interface TriviaQuestion {

  fun askQuestion(): Unit

  // Returns true if the answer is acceptable for this question. A
  // multiple choice question should return true for "A", "B", "C",
  // and "D", but not for "7". A numerical question should return true
  // for numbers only.
  fun acceptAnswer(answer: String): AnswerResult

  // Returns true if the answer is a correct answer for this question.
  // This does not have to be unique, so for instance a multiple
  // choice question might accept both "A" and "a" as correct.
  //
  // Precondition: acceptAnswer(answer).isSuccessful() is true.
  fun checkAnswer(answer: String): Boolean

  // The "canonical" correct answer. This will be displayed at the end
  // of the question.
  val canonicalAnswer: String

  fun chooseReward(): TriviaQuestionReward

}
