
package com.mercerenies.turtletroll.pot

import com.mercerenies.turtletroll.gravestone.CustomDeathMessageRegistry
import com.mercerenies.turtletroll.feature.container.withPlayerDebugCommand
import com.mercerenies.turtletroll.feature.container.FeatureContainer
import com.mercerenies.turtletroll.feature.container.ListenerContainer
import com.mercerenies.turtletroll.feature.builder.BuilderState
import com.mercerenies.turtletroll.feature.builder.FeatureBuilder
import com.mercerenies.turtletroll.feature.builder.FeatureContainerFactory
import com.mercerenies.turtletroll.Weight

import org.bukkit.Bukkit

object PotteryListenerFactory : FeatureContainerFactory<FeatureContainer> {
  override fun create(state: BuilderState): FeatureContainer {
    val replaceChance = state.config.getDouble("pottery.replace_chance")
    val infectedChance = state.config.getDouble("pottery.infected_chance")
    val potteryListener = PotteryListener(
        plugin = state.plugin,
        mobReplaceChance = replaceChance,
    )
    val infectedBreakAction = PotteryInfectedBreakAction(potteryListener)
    return FeatureBuilder()
        .addListener(potteryListener)
        .addFeature(potteryListener)
        .addPostRule(potteryListener.breakAction)
        .addBreakAction(Weight(infectedBreakAction, infectedChance))
        .build()
        .withPlayerDebugCommand("pottery") { player ->
          PotteryIdentifier(state.plugin).spawnPottery(player.location.block)
          true
        }
  }
}
