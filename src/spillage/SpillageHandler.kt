
package com.mercerenies.turtletroll.spillage

import org.bukkit.entity.Item

interface SpillageHandler {

  fun matches(item: Item): Boolean

  fun run(item: Item)

}
