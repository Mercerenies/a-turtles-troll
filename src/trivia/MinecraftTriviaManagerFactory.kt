
package com.mercerenies.turtletroll.trivia

import com.mercerenies.turtletroll.feature.container.FeatureContainer
import com.mercerenies.turtletroll.feature.builder.FeatureBuilder
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

  abstract val questionSupplier: TriviaQuestionSupplier

  override fun create(state: BuilderState): FeatureContainer {
    val config = TriviaConfig.fromGlobalConfig(state.config)
    val manager = MinecraftTriviaManager(state.plugin, config, questionSupplier)
    return FeatureBuilder()
      .addFeature(manager)
      .addCommand("answer" to manager.answerCommand.withPermission(Permissions.ANSWER))
      .addDebugCommand("triviaask" to manager.triviaAskCommand)
      .addDebugCommand("triviajudge" to manager.triviaJudgeCommand)
      .addRandomEvent(manager.triviaRandomEvent)
      .build()
  }

}
