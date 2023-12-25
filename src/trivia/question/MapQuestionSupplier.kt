
package com.mercerenies.turtletroll.trivia.question

import kotlin.collections.Map

class MapQuestionSupplier(
  private val backingMap: Map<String, () -> TriviaQuestion>,
) : TriviaQuestionSupplier {

  override val ids: Collection<String>
    get() = backingMap.keys

  override fun supply(): TriviaQuestion {
    val questionFunction = backingMap.values.random()
    return questionFunction()
  }

  override fun supply(id: String): TriviaQuestion {
    val questionFunction = backingMap[id]
    if (questionFunction == null) {
      throw NoSuchElementException("No question with id $id")
    } else {
      return questionFunction()
    }
  }

}
