
package com.mercerenies.turtletroll.feature.container

import com.mercerenies.turtletroll.feature.Feature
import com.mercerenies.turtletroll.feature.Schedulable
import com.mercerenies.turtletroll.feature.GameModification
import com.mercerenies.turtletroll.command.Command
import com.mercerenies.turtletroll.command.PermittedCommand
import com.mercerenies.turtletroll.util.lazyFlatten
import com.mercerenies.turtletroll.drop.BlockBreakAction
import com.mercerenies.turtletroll.Weight

import org.bukkit.event.Listener

import com.comphenix.protocol.events.PacketListener

// As CompositeFeatureContianer but for DropFeatureContainer.
class CompositeDropFeatureContainer(
  private val allContainers: Iterable<DropFeatureContainer>,
) : DropFeatureContainer {

  override val listeners: Iterable<Listener> =
    allContainers.map { it.listeners }.lazyFlatten()

  override val features: Iterable<Feature> =
    allContainers.map { it.features }.lazyFlatten()

  override val runnables: Iterable<Schedulable> =
    allContainers.map { it.runnables }.lazyFlatten()

  override val packetListeners: Iterable<PacketListener> =
    allContainers.map { it.packetListeners }.lazyFlatten()

  override val gameModifications: Iterable<GameModification> =
    allContainers.map { it.gameModifications }.lazyFlatten()

  override val commands: Iterable<Pair<String, PermittedCommand<Command>>> =
    allContainers.map { it.commands }.lazyFlatten()

  override val debugCommands: Iterable<Pair<String, Command>> =
    allContainers.map { it.debugCommands }.lazyFlatten()

  override val preRules: Iterable<BlockBreakAction> =
    allContainers.map { it.preRules }.lazyFlatten()

  override val actions: Iterable<Weight<BlockBreakAction>> =
    allContainers.map { it.actions }.lazyFlatten()

  override val postRules: Iterable<BlockBreakAction> =
    allContainers.map { it.postRules }.lazyFlatten()

}
