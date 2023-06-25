
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
    },
    {
      MultipleChoiceQuestion(
        questionBody = "What is the most common type of armor to spawn naturally on zombies?",
        answers = listOf("Leather", "Gold", "Chainmail", "Iron", "Diamond"),
        correctAnswerIndex = 1,
        rewards = listOf(ItemReward(Material.GOLDEN_BOOTS), ItemReward(Material.GOLDEN_HELMET), ItemReward(Material.GOLDEN_LEGGINGS), ItemReward(Material.GOLDEN_CHESTPLATE)),
      )
    },
    {
      NumericalQuestion(
        questionBody = "How many signs can be stacked in a single inventory slot?",
        correctAnswer = 16,
        rewards = BlockTypes.WOODEN_SIGNS.toList().map { ItemReward(it) },
      )
    }
  )

  val SUPPLIER: TriviaQuestionSupplier = TriviaQuestionSupplier {
    val questionFunction = QUESTIONS.sample()!!
    questionFunction()
  }

}
