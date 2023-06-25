
package com.mercerenies.turtletroll.trivia.question

import com.mercerenies.turtletroll.ext.*
import com.mercerenies.turtletroll.BlockTypes

import org.bukkit.Material

object QuestionLibrary {

  val QUESTIONS: List<() -> TriviaQuestion> = listOf(
    {
      MultipleChoiceQuestion(
        questionBody = "At what difficulty does the world need to be set to for zombies to break down doors?",
        answers = listOf("Peaceful", "Easy", "Medium", "Hard"),
        correctAnswerIndex = 3,
        rewards = BlockTypes.WOODEN_DOORS.toList().map { ItemReward(it) } + listOf(ItemReward(Material.ZOMBIE_SPAWN_EGG)),
        shuffleAnswers = false,
      )
    }
  )

  val SUPPLIER: TriviaQuestionSupplier = TriviaQuestionSupplier {
    val questionFunction = QUESTIONS.sample()!!
    questionFunction()
  }

}
