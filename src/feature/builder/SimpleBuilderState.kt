
package com.mercerenies.turtletroll.feature.builder

import com.mercerenies.turtletroll.storage.GlobalDataHolder

import org.bukkit.plugin.Plugin

class SimpleBuilderState(
  override val plugin: Plugin,
  override val dataStore: GlobalDataHolder,
) : BuilderState
