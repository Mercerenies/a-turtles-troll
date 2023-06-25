
package com.mercerenies.turtletroll.trivia

import com.mercerenies.turtletroll.feature.container.FeatureContainer
import com.mercerenies.turtletroll.feature.container.AbstractFeatureContainer
import com.mercerenies.turtletroll.feature.builder.BuilderState
import com.mercerenies.turtletroll.feature.builder.FeatureContainerFactory
import com.mercerenies.turtletroll.command.withPermission
import com.mercerenies.turtletroll.trivia.question.TriviaQuestionSupplier
import com.mercerenies.turtletroll.trivia.question.QuestionLibrary

abstract class MinecraftTriviaManagerFactory() : FeatureContainerFactory<FeatureContainer> {

  companion object : MinecraftTriviaManagerFactory() {
    val COMMAND_PERMISSION = "com.mercerenies.turtletroll.answer"

    override val config: TriviaConfig =
      TriviaConfig.DEFAULT

    override val questionSupplier: TriviaQuestionSupplier =
      QuestionLibrary.SUPPLIER

  }

  private class Container(
    private val manager: MinecraftTriviaManager,
  ) : AbstractFeatureContainer() {

    override val features =
      listOf(manager)

    override val runnables =
      listOf(manager)

    override val commands =
      listOf(
        "answer" to manager.answerCommand.withPermission(COMMAND_PERMISSION),
      )

  }

  abstract val config: TriviaConfig

  abstract val questionSupplier: TriviaQuestionSupplier

  override fun create(state: BuilderState): FeatureContainer {
    val manager = MinecraftTriviaManager(state.plugin, config, questionSupplier)
    return Container(manager)
  }

}
