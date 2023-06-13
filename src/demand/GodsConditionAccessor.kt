
package com.mercerenies.turtletroll.demand

import com.mercerenies.turtletroll.ext.*

import org.bukkit.Bukkit
import org.bukkit.NamespacedKey
import org.bukkit.boss.BarColor
import org.bukkit.boss.BarStyle
import org.bukkit.entity.Player

// Interface for things capable of asking how the gods are feeling
// right now.
interface GodsConditionAccessor {

  object AlwaysAppeased : GodsConditionAccessor {
    override fun getGodsStatus(): GodsStatus =
      GodsStatus.APPEASED
  }

  fun getGodsStatus(): GodsStatus

  fun isAngry(): Boolean =
    getGodsStatus() == GodsStatus.ANGRY

}

enum class GodsStatus {
  APPEASED, ANGRY, IDLE,
}
