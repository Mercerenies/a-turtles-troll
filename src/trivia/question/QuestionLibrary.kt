
package com.mercerenies.turtletroll.trivia.question

import com.mercerenies.turtletroll.ext.*

object QuestionLibrary {

  val QUESTIONS: List<TriviaQuestion> = listOf(

  )

  val SUPPLIER: TriviaQuestionSupplier = TriviaQuestionSupplier {
    QUESTIONS.sample()!!
  }

}
