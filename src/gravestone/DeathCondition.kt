
package com.mercerenies.turtletroll.gravestone

import org.bukkit.entity.EntityType
import org.bukkit.event.entity.EntityDamageEvent

interface DeathCondition {

  // Should be a prepositional phrase
  val description: String

  fun test(cause: CauseOfDeath): Boolean

  object True : DeathCondition {
    override val description: String = "for any reason"

    override fun test(cause: CauseOfDeath): Boolean = true

  }

  object MustBeAngel : DeathCondition {
    override val description: String = "to a Weeping Angel"

    override fun test(cause: CauseOfDeath): Boolean =
      cause is Angel

  }

  object MustBeMimic : DeathCondition {
    override val description: String = "to a Mimic"

    override fun test(cause: CauseOfDeath): Boolean =
      cause is Mimic

  }

  object MustBeVector : DeathCondition {
    override val description: String = "to Vector"

    override fun test(cause: CauseOfDeath): Boolean =
      cause is VanillaMob &&
      cause.entityType == EntityType.PHANTOM

  }

  object FireDamage : DeathCondition {
    val conditions = listOf(
      EntityDamageEvent.DamageCause.FIRE, EntityDamageEvent.DamageCause.FIRE_TICK,
      EntityDamageEvent.DamageCause.HOT_FLOOR, EntityDamageEvent.DamageCause.LAVA,
    )

    override val description: String = "to fire damage"

    override fun test(cause: CauseOfDeath): Boolean =
      cause is Vanilla &&
      conditions.contains(cause.cause)

  }

  object Explosion : DeathCondition {
    val conditions = listOf(
      EntityDamageEvent.DamageCause.ENTITY_EXPLOSION, EntityDamageEvent.DamageCause.BLOCK_EXPLOSION,
    )

    override val description: String = "to an explosion"

    override fun test(cause: CauseOfDeath): Boolean =
      cause is Vanilla &&
      conditions.contains(cause.cause)

  }

  object Drowning : DeathCondition {

    override val description: String = "by drowning"

    override fun test(cause: CauseOfDeath): Boolean =
      cause is Vanilla &&
      cause.cause == EntityDamageEvent.DamageCause.DROWNING

  }

  object Falling : DeathCondition {

    override val description: String = "by falling"

    override fun test(cause: CauseOfDeath): Boolean =
      cause is Vanilla &&
      cause.cause == EntityDamageEvent.DamageCause.FALL

  }

  object MustBeZombie : DeathCondition {
    val conditions = listOf(
      EntityType.ZOMBIE, EntityType.ZOMBIE_VILLAGER
    )

    override val description: String = "to a zombie"

    override fun test(cause: CauseOfDeath): Boolean =
      cause is VanillaMob &&
      conditions.contains(cause.entityType)

  }

  object MustBeBee : DeathCondition {

    override val description: String = "to a bee"

    override fun test(cause: CauseOfDeath): Boolean =
      cause is VanillaMob &&
      cause.entityType == EntityType.BEE

  }

  object MustBeEnderman : DeathCondition {

    override val description: String = "to an Enderman"

    override fun test(cause: CauseOfDeath): Boolean =
      cause is VanillaMob &&
      cause.entityType == EntityType.ENDERMAN

  }

  object MustBeGhast : DeathCondition {

    override val description: String = "to a Ghast"

    override fun test(cause: CauseOfDeath): Boolean =
      cause is VanillaMob &&
      cause.entityType == EntityType.GHAST

  }

  object MustBeRavager : DeathCondition {

    override val description: String = "to a Ravager"

    override fun test(cause: CauseOfDeath): Boolean =
      cause is VanillaMob &&
      cause.entityType == EntityType.RAVAGER

  }

  object MustBeIronGolem : DeathCondition {

    override val description: String = "to an Iron Golem"

    override fun test(cause: CauseOfDeath): Boolean =
      cause is VanillaMob &&
      cause.entityType == EntityType.IRON_GOLEM

  }

  object MustBeSilverfish : DeathCondition {

    override val description: String = "to Silverfish"

    override fun test(cause: CauseOfDeath): Boolean =
      cause is VanillaMob &&
      cause.entityType == EntityType.SILVERFISH

  }

  object MustBeBlaze : DeathCondition {

    override val description: String = "to a Blaze"

    override fun test(cause: CauseOfDeath): Boolean =
      cause is VanillaMob &&
      cause.entityType == EntityType.BLAZE

  }

}
