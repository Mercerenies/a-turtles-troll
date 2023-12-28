
package com.mercerenies.turtletroll.demand

import com.mercerenies.turtletroll.util.component.*

import org.bukkit.Bukkit
import org.bukkit.NamespacedKey
import org.bukkit.boss.BarColor
import org.bukkit.boss.BarStyle
import org.bukkit.entity.Player

class DailyDemandBossBarUpdater(
  val namespacedKey: NamespacedKey,
) {

  companion object {

    fun barColor(state: GodsStatus): BarColor =
      when (state) {
        GodsStatus.IDLE -> BarColor.YELLOW
        GodsStatus.APPEASED -> BarColor.YELLOW
        GodsStatus.ANGRY -> BarColor.RED
      }

  }

  private var isSetToVisible = true
  private val bossBar = Bukkit.createBossBar(
    namespacedKey,
    "Appeased",
    BarColor.GREEN,
    BarStyle.SOLID,
  )
  private var summary: String = "Die"
  private var status: GodsStatus = GodsStatus.APPEASED

  init {
    this.setVisible(true)
    bossBar.setProgress(1.0)
    this.updatePlayerList()
  }

  val barMessage: String
    get() = when (status) {
      GodsStatus.IDLE -> summary
      GodsStatus.APPEASED -> "Appeased"
      GodsStatus.ANGRY -> "The gods are angry!"
    }

  private fun updateStats() {
    bossBar.setVisible(isSetToVisible && (status != GodsStatus.APPEASED))
    bossBar.setColor(barColor(status))
    bossBar.setTitle(barMessage)
  }

  fun setVisible(visible: Boolean) {
    isSetToVisible = visible
    updateStats()
  }

  fun updateCondition(state: GodsStatus, event: DailyDemandEvent? = null) {
    if (event != null) {
      summary = event.summary
    }
    status = state
    updateStats()
  }

  // Assume the player list is invalid and get a completely new one.
  // This is called when the object is first constructed but should
  // also be called if the DailyDemandManager feature has just been
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
