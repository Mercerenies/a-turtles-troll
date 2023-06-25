
package com.mercerenies.turtletroll.trivia.question

import org.bukkit.entity.Player

import net.kyori.adventure.text.Component

interface TriviaQuestionReward {

  val name: Component

  fun rewardPlayer(player: Player): Unit

}
