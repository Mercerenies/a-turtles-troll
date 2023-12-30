
package com.mercerenies.turtletroll.trivia

import com.mercerenies.turtletroll.feature.container.FeatureContainer
import com.mercerenies.turtletroll.feature.container.AbstractFeatureContainer
import com.mercerenies.turtletroll.feature.builder.BuilderState
import com.mercerenies.turtletroll.feature.builder.FeatureContainerFactory
import com.mercerenies.turtletroll.command.withPermission
import com.mercerenies.turtletroll.command.Permissions
import com.mercerenies.turtletroll.trivia.question.TriviaQuestionSupplier
import com.mercerenies.turtletroll.trivia.question.QuestionLibrary

abstract class MinecraftTriviaManagerFactory() : FeatureContainerFactory<FeatureContainer> {

  companion object : MinecraftTriviaManagerFactory() {

    override val questionSupplier: TriviaQuestionSupplier =
      QuestionLibrary.SUPPLIER

  }

  private class Container(
    private val manager: MinecraftTriviaManager,
  ) : AbstractFeatureContainer() {

    override val features =
      listOf(manager)

    override val commands =
      listOf(
        "answer" to manager.answerCommand.withPermission(Permissions.ANSWER),
      )

    override val debugCommands =
      listOf(
        "triviaask" to manager.triviaAskCommand,
        "triviajudge" to manager.triviaJudgeCommand,
      )

    override val randomEvents =
      listOf(manager.triviaRandomEvent)

  }

  abstract val questionSupplier: TriviaQuestionSupplier

  override fun create(state: BuilderState): FeatureContainer {
    val config = TriviaConfig.fromGlobalConfig(state.config)
    val manager = MinecraftTriviaManager(state.plugin, config, questionSupplier)
    return Container(manager)
  }

}
