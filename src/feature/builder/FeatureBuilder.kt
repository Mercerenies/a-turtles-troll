
package com.mercerenies.turtletroll.feature.builder

import com.mercerenies.turtletroll.feature.Feature
import com.mercerenies.turtletroll.feature.container.FeatureContainer
import com.mercerenies.turtletroll.feature.GameModification
import com.mercerenies.turtletroll.command.Command
import com.mercerenies.turtletroll.command.PermittedCommand
import com.mercerenies.turtletroll.Weight
import com.mercerenies.turtletroll.drop.BlockBreakAction
import com.mercerenies.turtletroll.happening.RandomEvent

import org.bukkit.event.Listener

import com.comphenix.protocol.events.PacketListener

import kotlin.collections.ArrayList

// Builder API for FeatureContainer.
class FeatureBuilder {

  private class Container : FeatureContainer {
    override val listeners = ArrayList<Listener>()
    override val features = ArrayList<Feature>()
    override val packetListeners = ArrayList<PacketListener>()
    override val gameModifications = ArrayList<GameModification>()
    override val commands = ArrayList<Pair<String, PermittedCommand<Command>>>()
    override val debugCommands = ArrayList<Pair<String, Command>>()
    override val preRules = ArrayList<BlockBreakAction>()
    override val actions = ArrayList<Weight<BlockBreakAction>>()
    override val postRules = ArrayList<BlockBreakAction>()
    override val randomEvents = ArrayList<RandomEvent>()
  }

  private var built = false
  private val container = Container()

  private fun assertNotBuilt() {
    if (built) {
      throw IllegalStateException("FeatureBuilder has already been consumed")
    }
  }

  fun addListener(vararg listeners: Listener): FeatureBuilder {
    assertNotBuilt()
    container.listeners.addAll(listeners)
    return this
  }

  fun addFeature(vararg features: Feature): FeatureBuilder {
    assertNotBuilt()
    container.features.addAll(features)
    return this
  }

  fun addPacketListener(vararg listeners: PacketListener): FeatureBuilder {
    assertNotBuilt()
    container.packetListeners.addAll(listeners)
    return this
  }

  fun addGameModification(vararg modifications: GameModification): FeatureBuilder {
    assertNotBuilt()
    container.gameModifications.addAll(modifications)
    return this
  }

  fun addCommand(vararg commands: Pair<String, PermittedCommand<Command>>): FeatureBuilder {
    assertNotBuilt()
    container.commands.addAll(commands)
    return this
  }

  fun addDebugCommand(vararg commands: Pair<String, Command>): FeatureBuilder {
    assertNotBuilt()
    container.debugCommands.addAll(commands)
    return this
  }

  fun addPreRule(vararg actions: BlockBreakAction): FeatureBuilder {
    assertNotBuilt()
    container.preRules.addAll(actions)
    return this
  }

  fun addBreakAction(vararg actions: Weight<BlockBreakAction>): FeatureBuilder {
    assertNotBuilt()
    container.actions.addAll(actions)
    return this
  }

  fun addPostRule(vararg actions: BlockBreakAction): FeatureBuilder {
    assertNotBuilt()
    container.postRules.addAll(actions)
    return this
  }

  fun addRandomEvent(vararg events: RandomEvent): FeatureBuilder {
    assertNotBuilt()
    container.randomEvents.addAll(events)
    return this
  }

  // Note: The builder is consumed by this method. No other methods
  // can be called on the builder after this one.
  fun build(): FeatureContainer {
    assertNotBuilt()
    built = true
    return container
  }

}
