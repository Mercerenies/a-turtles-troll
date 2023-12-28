
package com.mercerenies.turtletroll.trivia.question

sealed interface AnswerResult {
  fun isSuccessful(): Boolean
  val errorMessage: String

  object SuccessResult : AnswerResult {
    override fun isSuccessful(): Boolean = true
    override val errorMessage: String = ""
  }

  class ErrorResult(
    override val errorMessage: String,
  ) : AnswerResult {
    override fun isSuccessful(): Boolean = false
  }

}
