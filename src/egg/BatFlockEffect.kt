
package com.mercerenies.turtletroll.egg

import org.bukkit.Location
import org.bukkit.entity.Entity
import org.bukkit.entity.Bat

import kotlin.reflect.KClass
import kotlin.random.Random

open class BatFlockEffect(
  val count: Int,
) : EggHatchEffect {

  override fun onEggHatch(loc: Location) {
    repeat(count) {
      val entity = loc.world!!.spawn(loc, Bat::class.java)
      onResultingEntity(entity)
    }
  }

  open fun onResultingEntity(entity: Bat) {
    entity.setAwake(true)
  }

}
