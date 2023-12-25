
package com.mercerenies.turtletroll.trivia.question

interface TriviaQuestionSupplier {

  val ids: Collection<String>

  fun supply(): TriviaQuestion

  // Supplies the question with the given ID. Throws
  // NoSuchElementException if no question exists with that ID.
  fun supply(id: String): TriviaQuestion

}
