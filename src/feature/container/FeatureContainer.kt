
package com.mercerenies.turtletroll.feature.container

import com.mercerenies.turtletroll.feature.Feature
import com.mercerenies.turtletroll.feature.Schedulable
import com.mercerenies.turtletroll.feature.GameModification
import com.mercerenies.turtletroll.command.Command
import com.mercerenies.turtletroll.command.PermittedCommand
import com.mercerenies.turtletroll.Weight
import com.mercerenies.turtletroll.drop.BlockBreakAction
import com.mercerenies.turtletroll.happening.RandomEvent

import org.bukkit.event.Listener

import com.comphenix.protocol.events.PacketListener

interface FeatureContainer {

  val listeners: Iterable<Listener>

  val features: Iterable<Feature>

  val runnables: Iterable<Schedulable>

  val packetListeners: Iterable<PacketListener>

  val gameModifications: Iterable<GameModification>

  val commands: Iterable<Pair<String, PermittedCommand<Command>>>

  // As `commands`, but goes in the `/turtle dbg` namespace rather
  // than `/turtle`. All commands in `debugCommands` automatically get
  // the "DEBUG" permission level.
  val debugCommands: Iterable<Pair<String, Command>>

  val preRules: Iterable<BlockBreakAction>

  val actions: Iterable<Weight<BlockBreakAction>>

  val postRules: Iterable<BlockBreakAction>

  val randomEvents: Iterable<RandomEvent>

}
