
package com.mercerenies.turtletroll.trivia

import com.mercerenies.turtletroll.ext.*
import com.mercerenies.turtletroll.util.*
import com.mercerenies.turtletroll.gravestone.CustomDeathMessageRegistry
import com.mercerenies.turtletroll.gravestone.CustomDeathMessage
import com.mercerenies.turtletroll.gravestone.Angel
import com.mercerenies.turtletroll.feature.RunnableFeature
import com.mercerenies.turtletroll.SpawnReason
import com.mercerenies.turtletroll.Constants
import com.mercerenies.turtletroll.trivia.question.TriviaQuestionSupplier

import org.bukkit.entity.Player
import org.bukkit.entity.ArmorStand
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.EntityType
import org.bukkit.plugin.Plugin
import org.bukkit.Location
import org.bukkit.Color
import org.bukkit.Bukkit
import org.bukkit.Particle
import org.bukkit.event.Listener
import org.bukkit.event.EventHandler
import org.bukkit.event.player.PlayerMoveEvent
import org.bukkit.event.entity.EntityDeathEvent
import org.bukkit.event.entity.CreatureSpawnEvent
import org.bukkit.Sound
import org.bukkit.util.EulerAngle

import net.kyori.adventure.text.Component

import kotlin.collections.HashMap
import kotlin.random.Random

class MinecraftTriviaManager(
  plugin: Plugin,
  val config: TriviaConfig,
  questionSupplier: TriviaQuestionSupplier,
) : RunnableFeature(plugin) {

  override val name = "minecrafttrivia"

  override val description = "The game will regularly ask trivia questions and reward those who get it right"

  // Run once per minute
  override val taskPeriod = Constants.TICKS_PER_SECOND * 60L

  private var state: TriviaState = config.initialState

  private val engine: TriviaEngine = TriviaEngine(questionSupplier)

  override fun run() {
    if (!isEnabled()) {
      return
    }

    val transition = state.advance(config)
    transition.perform(engine)
    state = transition.nextState
  }

}
