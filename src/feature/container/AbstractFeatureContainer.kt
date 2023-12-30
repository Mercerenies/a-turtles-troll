
package com.mercerenies.turtletroll.feature.container

import com.mercerenies.turtletroll.feature.Feature
import com.mercerenies.turtletroll.feature.GameModification
import com.mercerenies.turtletroll.command.Command
import com.mercerenies.turtletroll.command.PermittedCommand
import com.mercerenies.turtletroll.drop.BlockBreakAction
import com.mercerenies.turtletroll.Weight
import com.mercerenies.turtletroll.happening.RandomEvent

import org.bukkit.event.Listener

import com.comphenix.protocol.events.PacketListener

// Abstract parent version of FeatureContainer which has trivially
// empty implementations for all methods
open class AbstractFeatureContainer() : FeatureContainer {

  open override val listeners: Iterable<Listener>
    get() = listOf()

  open override val features: Iterable<Feature>
    get() = listOf()

  open override val packetListeners: Iterable<PacketListener>
    get() = listOf()

  open override val gameModifications: Iterable<GameModification>
    get() = listOf()

  open override val commands: Iterable<Pair<String, PermittedCommand<Command>>>
    get() = listOf()

  open override val debugCommands: Iterable<Pair<String, Command>>
    get() = listOf()

  open override val preRules: List<BlockBreakAction>
    get() = listOf()

  open override val actions: List<Weight<BlockBreakAction>>
    get() = listOf()

  open override val postRules: List<BlockBreakAction>
    get() = listOf()

  open override val randomEvents: Iterable<RandomEvent>
    get() = listOf()

}
