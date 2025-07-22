
package com.mercerenies.turtletroll

import org.bukkit.Keyed

import io.papermc.paper.registry.RegistryKey
import io.papermc.paper.registry.RegistryAccess

// Convenience class to access Paper's obtuse registry API.
object PaperRegistry {
  fun<T: Keyed> valuesOf(key: RegistryKey<T>): List<T> =
      RegistryAccess.registryAccess().getRegistry(key)
      .iterator()
      .asSequence()
      .toList()
}
