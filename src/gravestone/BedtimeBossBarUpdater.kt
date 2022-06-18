
package com.mercerenies.turtletroll.gravestone

import com.mercerenies.turtletroll.ext.*

import org.bukkit.Bukkit
import org.bukkit.NamespacedKey
import org.bukkit.boss.BarColor
import org.bukkit.boss.BarStyle
import org.bukkit.entity.Player

class BedtimeBossBarUpdater(
  val namespacedKey: NamespacedKey,
) {

  companion object {

    fun barColor(state: Status): BarColor =
      when (state) {
        Status.WAITING -> BarColor.YELLOW
        Status.APPEASED -> BarColor.YELLOW
        Status.ANGRY -> BarColor.RED
      }

  }

  enum class Status {
    WAITING, APPEASED, ANGRY,
  }

  private var isSetToVisible = true
  private val bossBar = Bukkit.createBossBar(
    namespacedKey,
    "Appeased",
    BarColor.GREEN,
    BarStyle.SOLID,
  )
  private var deathCondition: DeathCondition = DeathCondition.True
  private var status: Status = Status.APPEASED

  init {
    this.setVisible(true)
    bossBar.setProgress(1.0)
    this.updatePlayerList()
  }

  val barMessage: String
    get() = when (status) {
      Status.WAITING -> deathCondition.summary
      Status.APPEASED -> "Appeased"
      Status.ANGRY -> "The gods are angry!"
    }

  private fun updateStats() {
    bossBar.setVisible(isSetToVisible && (status != Status.APPEASED))
    bossBar.setColor(barColor(status))
    bossBar.setTitle(barMessage)
  }

  fun setVisible(visible: Boolean) {
    isSetToVisible = visible
    updateStats()
  }

  fun updateCondition(state: Status, condition: DeathCondition? = null) {
    if (condition != null) {
      deathCondition = condition
    }
    status = state
    updateStats()
  }

  // Assume the player list is invalid and get a completely new one.
  // This is called when the object is first constructed but should
  // also be called if the BedtimeManager feature has just been
  // enabled, for instance, and has not been sending player join/leave
  // events during the time it was disabled.
  fun updatePlayerList() {
    bossBar.removeAll()
    for (player in Bukkit.getOnlinePlayers()) {
      bossBar.addPlayer(player)
    }
  }

  fun addPlayer(player: Player) {
    bossBar.addPlayer(player)
  }

  fun removePlayer(player: Player) {
    bossBar.removePlayer(player)
  }

}
