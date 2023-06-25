
package com.mercerenies.turtletroll.demand

import com.mercerenies.turtletroll.ScheduledEventRunnable
import com.mercerenies.turtletroll.command.PermittedCommand
import com.mercerenies.turtletroll.command.Command
import com.mercerenies.turtletroll.command.TerminalCommand
import com.mercerenies.turtletroll.command.withPermission
import com.mercerenies.turtletroll.command.Permissions
import com.mercerenies.turtletroll.ext.*
import com.mercerenies.turtletroll.Messages
import com.mercerenies.turtletroll.demand.event.EventSelector

import org.bukkit.plugin.Plugin
import org.bukkit.NamespacedKey
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.entity.PlayerDeathEvent
import org.bukkit.event.player.PlayerBedEnterEvent
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent
import org.bukkit.event.Listener
import org.bukkit.command.CommandSender

import net.kyori.adventure.text.Component

class DailyDemandManager(
  plugin: Plugin,
  private val eventSelector: EventSelector,
) : ScheduledEventRunnable<DailyDemandManager.State>(plugin), Listener, GodsState {

  companion object {
    val DAWN_TIME = 0L
    val DUSK_TIME = 12000L

    val ANGRY_MESSAGE = Component.text("The gods are angry; no one shall sleep tonight!")

    val SATISFIED_MESSAGE = Component.text("The gods are appeased today; everyone is free to sleep.")

    val DISABLED_MESSAGE = Component.text("This feature is currently disabled; the gods are not interfering with your sleep.")

    val STATES = listOf(
      Event(State.Daytime, DAWN_TIME),
      Event(State.Nighttime, DUSK_TIME),
    )

  }

  enum class State {
    Daytime,
    Nighttime,
  }

  override val name: String = "demand"

  override val description: String = "The gods must be appeased with a condition in order to allow players to sleep"

  private var isAppeased: Boolean = true

  private var condition: DailyDemandEvent = DeathCondition.True

  private val bossBarKey: NamespacedKey = NamespacedKey(plugin, "com.mercerenies.turtletroll.demand.DailyDemandManager.bossBarKey")
  private val bossBar: DailyDemandBossBarUpdater = DailyDemandBossBarUpdater(bossBarKey)

  val DemandCommand = object : TerminalCommand() {

    private fun getMessageToSend(): Component =
      if (!isEnabled()) {
        DISABLED_MESSAGE
      } else if (isAppeased) {
        SATISFIED_MESSAGE
      } else {
        when (currentState) {
          State.Daytime -> {
            condition.getRequestMessage()
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
    get() = "demand" to DemandCommand.withPermission(Permissions.DEMAND)

  override fun getGodsStatus(): GodsStatus =
    when {
      !isEnabled() -> GodsStatus.IDLE
      isAppeased -> GodsStatus.APPEASED
      currentState == State.Daytime -> GodsStatus.IDLE
      else -> GodsStatus.ANGRY
    }

  override fun setGodsAppeased(isAppeased: Boolean) {
    if (isAppeased && !this.isAppeased) {
      eventSelector.onGodsAppeased()
    }
    this.isAppeased = isAppeased
    bossBar.updateCondition(getGodsStatus())
  }

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
        condition = eventSelector.chooseCondition()
        bossBar.updateCondition(GodsStatus.IDLE, condition)
        condition.onDayStart(this)
      }
      State.Nighttime -> {
        condition.onDayEnd(this)
        if (!isAppeased) {
          bossBar.updateCondition(GodsStatus.ANGRY)
          eventSelector.onGodsAngered()
        }
      }
    }
  }

  @EventHandler(priority = EventPriority.HIGH)
  fun onPlayerDeath(event: PlayerDeathEvent) {
    if (!isEnabled()) {
      return
    }

    if (currentState == State.Daytime) {
      condition.onDaytimePlayerDeath(event, this)
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
