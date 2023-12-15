
package com.mercerenies.turtletroll.demand

import org.bukkit.event.entity.PlayerDeathEvent
import org.bukkit.event.block.BlockBreakEvent

import net.kyori.adventure.text.Component

interface DailyDemandEvent {

  // Should be a simple noun or verb phrase summarizing the condition
  // (For the boss bar)
  val summary: String

  fun getRequestMessage(): Component

  fun onDayStart(godsState: GodsState): Unit

  fun onDayEnd(godsState: GodsState): Unit

  fun onDaytimePlayerDeath(event: PlayerDeathEvent, godsState: GodsState) {}

  fun onDaytimeBlockBreak(event: BlockBreakEvent, godsState: GodsState) {}

}
