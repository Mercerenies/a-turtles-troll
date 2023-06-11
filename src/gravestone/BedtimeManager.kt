
package com.mercerenies.turtletroll.gravestone

import com.mercerenies.turtletroll.ScheduledEventRunnable
import com.mercerenies.turtletroll.command.PermittedCommand
import com.mercerenies.turtletroll.command.Command
import com.mercerenies.turtletroll.command.TerminalCommand
import com.mercerenies.turtletroll.command.withPermission
import com.mercerenies.turtletroll.Weight
import com.mercerenies.turtletroll.sample
import com.mercerenies.turtletroll.ext.*
import com.mercerenies.turtletroll.feature.container.FeatureContainer
import com.mercerenies.turtletroll.feature.container.AbstractFeatureContainer
import com.mercerenies.turtletroll.feature.builder.BuilderState
import com.mercerenies.turtletroll.feature.builder.FeatureContainerFactory
import com.mercerenies.turtletroll.Messages
import com.mercerenies.turtletroll.gravestone.condition.DifficultyClass

import org.bukkit.plugin.Plugin
import org.bukkit.Bukkit
import org.bukkit.NamespacedKey
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.entity.PlayerDeathEvent
import org.bukkit.event.player.PlayerBedEnterEvent
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent
import org.bukkit.event.Listener
import org.bukkit.command.CommandSender

class BedtimeManager(plugin: Plugin) : ScheduledEventRunnable<BedtimeManager.State>(plugin), Listener {

  companion object : FeatureContainerFactory<FeatureContainer> {
    val DAWN_TIME = 0L
    val DUSK_TIME = 12000L

    val CONDITION_LIST: List<Weight<DifficultyClass>> = listOf(
      Weight(DifficultyClass.EASY, 5.0),
      Weight(DifficultyClass.MEDIUM, 3.0),
      Weight(DifficultyClass.HARD, 1.0),
    )

    val ANGRY_MESSAGE = "The gods are angry; no one shall sleep tonight!"

    val SATISFIED_MESSAGE = "The gods are appeased today; everyone is free to sleep."

    val DISABLED_MESSAGE = "This feature is currently disabled; the gods are not interfering with your sleep."

    val COMMAND_PERMISSION = "com.mercerenies.turtletroll.command.bedtime"

    override fun create(state: BuilderState): FeatureContainer =
      Container(BedtimeManager(state.plugin))

    fun requestMessage(condition: DeathCondition): String =
      "Today, the gods would like to see someone die ${condition.description}"

    fun appeasedMessage(player: Player): String =
      "${player.displayName} has appeased the gods! You may sleep tonight."

    private fun chooseCondition(): DeathCondition =
      // Choose difficulty first, then choose a death condition in that difficulty tier
      sample(CONDITION_LIST).conditions.sample()!!

    val STATES = listOf(
      Event(State.Daytime, DAWN_TIME),
      Event(State.Nighttime, DUSK_TIME),
    )

  }

  private class Container(
    private val manager: BedtimeManager,
  ) : AbstractFeatureContainer() {

    override val listeners =
      listOf(manager)

    override val features =
      listOf(manager)

    override val runnables =
      listOf(manager)

    override val commands =
      listOf(manager.command)

  }

  enum class State {
    Daytime,
    Nighttime,
  }

  override val name: String = "bedtime"

  override val description: String = "The gods must be appeased with a condition in order to allow players to sleep"

  private var isAppeased: Boolean = true

  private var condition: DeathCondition = DeathCondition.True

  private val bossBarKey: NamespacedKey = NamespacedKey(plugin, "com.mercerenies.turtletroll.bedtime.BedtimeManager.bossBarKey")
  private val bossBar: BedtimeBossBarUpdater = BedtimeBossBarUpdater(bossBarKey)

  val BedtimeCommand = object : TerminalCommand() {

    private fun getMessageToSend(): String =
      if (!isEnabled()) {
        DISABLED_MESSAGE
      } else if (isAppeased) {
        SATISFIED_MESSAGE
      } else {
        when (currentState) {
          State.Daytime -> {
            requestMessage(condition)
          }
          State.Nighttime -> {
            ANGRY_MESSAGE
          }
        }
      }

    override fun onCommand(sender: CommandSender): Boolean {
      val message = getMessageToSend()
      Messages.sendMessage(sender, message)
      return true
    }

  }

  val command: Pair<String, PermittedCommand<Command>>
    get() = "bedtime" to BedtimeCommand.withPermission(COMMAND_PERMISSION)

  override fun enable() {
    super.enable()
    isAppeased = true
    bossBar.setVisible(true)
    bossBar.updatePlayerList()
  }

  override fun disable() {
    super.disable()
    bossBar.setVisible(false)
  }

  override fun getAllStates() = STATES

  override fun getDefaultState() = State.Nighttime

  override fun onStateShift(newState: State) {
    when (newState) {
      State.Daytime -> {
        isAppeased = false
        condition = chooseCondition()
        bossBar.updateCondition(BedtimeBossBarUpdater.Status.WAITING, condition)
        Messages.broadcastMessage(requestMessage(condition))
      }
      State.Nighttime -> {
        if (!isAppeased) {
          bossBar.updateCondition(BedtimeBossBarUpdater.Status.ANGRY)
          Messages.broadcastMessage(ANGRY_MESSAGE)
        }
      }
    }
  }

  @EventHandler(priority = EventPriority.HIGH)
  fun onPlayerDeath(event: PlayerDeathEvent) {
    if (!isEnabled()) {
      return
    }

    if ((currentState == State.Daytime) && (!isAppeased)) {
      val cause = CauseOfDeath.identify(event)
      if (condition.test(cause)) {
        Messages.broadcastMessage(appeasedMessage(event.entity))
        bossBar.updateCondition(BedtimeBossBarUpdater.Status.APPEASED)
        isAppeased = true
      }
    }

  }

  @EventHandler(priority = EventPriority.HIGH)
  fun onPlayerBedEnter(event: PlayerBedEnterEvent) {
    if (!isEnabled()) {
      return
    }

    if (!event.isCancelled()) {
      if (event.bedEnterResult == PlayerBedEnterEvent.BedEnterResult.OK) {
        // Check that the gods are happy
        if (!isAppeased) {
          event.setCancelled(true)
          Messages.sendMessage(event.player, ANGRY_MESSAGE)
        }
      }
    }

  }

  @EventHandler
  fun onPlayerJoin(event: PlayerJoinEvent) {
    if (!isEnabled()) {
      return
    }
    bossBar.addPlayer(event.player)
  }

  @EventHandler
  fun onPlayerQuit(event: PlayerQuitEvent) {
    if (!isEnabled()) {
      return
    }
    bossBar.removePlayer(event.player)
  }

}
