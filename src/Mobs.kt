
package com.mercerenies.turtletroll

import org.bukkit.entity.Entity
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Boss

object Mobs {

  fun isNonBossMob(entity: Entity): Boolean =
    entity is LivingEntity &&
    entity !is Boss

}
