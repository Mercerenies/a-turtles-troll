
package com.mercerenies.turtletroll.gravestone

import org.bukkit.entity.EntityType
import org.bukkit.event.entity.EntityDamageEvent

interface DeathCondition {

  // Should be a prepositional phrase
  val description: String

  // Should be a simple noun or verb phrase summarizing the condition
  val summary: String

  fun test(cause: CauseOfDeath): Boolean

  object True : DeathCondition {
    override val description: String = "for any reason"
    override val summary: String = "Die"

    override fun test(cause: CauseOfDeath): Boolean = true

  }

  object MustBeAngel : DeathCondition {
    override val description: String = "to a Weeping Angel"
    override val summary: String = "Die to Weeping Angel"

    override fun test(cause: CauseOfDeath): Boolean =
      cause is Angel

  }

  object MustBeMimic : DeathCondition {
    override val description: String = "to a Mimic"
    override val summary: String = "Die to Mimic"

    override fun test(cause: CauseOfDeath): Boolean =
      cause is Mimic

  }

  object MustBeVector : DeathCondition {
    override val description: String = "to Vector"
    override val summary: String = "Die to Vector"

    override fun test(cause: CauseOfDeath): Boolean =
      cause is VanillaMob &&
        cause.entityType == EntityType.PHANTOM

  }

  object MustBeRedstone : DeathCondition {
    override val description: String = "to redstone dust"
    override val summary: String = "Die to Redstone"

    override fun test(cause: CauseOfDeath): Boolean =
      cause is Redstone

  }

  object FireDamage : DeathCondition {
    val conditions = listOf(
      EntityDamageEvent.DamageCause.FIRE, EntityDamageEvent.DamageCause.FIRE_TICK,
      EntityDamageEvent.DamageCause.HOT_FLOOR, EntityDamageEvent.DamageCause.LAVA,
    )

    override val description: String = "to fire damage"
    override val summary: String = "Die to Fire"

    override fun test(cause: CauseOfDeath): Boolean =
      cause is Vanilla &&
        conditions.contains(cause.cause)

  }

  object Lightning : DeathCondition {
    val conditions = listOf(
      EntityDamageEvent.DamageCause.LIGHTNING,
    )

    override val description: String = "to lightning"
    override val summary: String = "Die to Lightning"

    override fun test(cause: CauseOfDeath): Boolean =
      (cause is Vanilla && conditions.contains(cause.cause))

  }

  object Hunger : DeathCondition {
    val conditions = listOf(
      EntityDamageEvent.DamageCause.STARVATION,
    )

    override val description: String = "to starvation"
    override val summary: String = "Die to Starvation"

    override fun test(cause: CauseOfDeath): Boolean =
      (cause is Vanilla && conditions.contains(cause.cause))

  }

  object Explosion : DeathCondition {
    val conditions = listOf(
      EntityDamageEvent.DamageCause.ENTITY_EXPLOSION, EntityDamageEvent.DamageCause.BLOCK_EXPLOSION,
    )

    override val description: String = "to an explosion"
    override val summary: String = "Die to Explosion"

    override fun test(cause: CauseOfDeath): Boolean =
      (cause is Vanilla && conditions.contains(cause.cause)) ||
        (cause is VanillaMob && cause.entityType == EntityType.CREEPER)

  }

  object Drowning : DeathCondition {

    override val description: String = "by drowning"
    override val summary: String = "Die by Drowning"

    override fun test(cause: CauseOfDeath): Boolean =
      cause is Vanilla &&
        cause.cause == EntityDamageEvent.DamageCause.DROWNING

  }

  object Falling : DeathCondition {

    override val description: String = "by falling"
    override val summary: String = "Die by Falling"

    override fun test(cause: CauseOfDeath): Boolean =
      cause is Vanilla &&
        cause.cause == EntityDamageEvent.DamageCause.FALL

  }

  object MustBeZombie : DeathCondition {
    val conditions = listOf(
      EntityType.ZOMBIE, EntityType.ZOMBIE_VILLAGER, EntityType.DROWNED,
      EntityType.ZOMBIFIED_PIGLIN, EntityType.HUSK,
    )

    override val description: String = "to a zombie"
    override val summary: String = "Die to Zombie"

    override fun test(cause: CauseOfDeath): Boolean =
      cause is VanillaMob &&
        conditions.contains(cause.entityType)

  }

  object MustBeLlama : DeathCondition {
    val conditions = listOf(
      EntityType.LLAMA, EntityType.LLAMA_SPIT, EntityType.TRADER_LLAMA,
    )

    override val description: String = "to a llama"
    override val summary: String = "Die to Llama"

    override fun test(cause: CauseOfDeath): Boolean =
      cause is VanillaMob &&
        conditions.contains(cause.entityType)

  }

  object MustBeBee : DeathCondition {

    override val description: String = "to a bee"
    override val summary: String = "Die to Bee"

    override fun test(cause: CauseOfDeath): Boolean =
      cause is VanillaMob &&
        cause.entityType == EntityType.BEE

  }

  object MustBeSpider : DeathCondition {

    override val description: String = "to a spider"
    override val summary: String = "Die to Spider"

    override fun test(cause: CauseOfDeath): Boolean =
      cause is VanillaMob &&
        (cause.entityType == EntityType.SPIDER || cause.entityType == EntityType.CAVE_SPIDER)

  }

  object MustBeEnderman : DeathCondition {

    override val description: String = "to an Enderman"
    override val summary: String = "Die to Enderman"

    override fun test(cause: CauseOfDeath): Boolean =
      cause is VanillaMob &&
        cause.entityType == EntityType.ENDERMAN

  }

  object MustBeGhast : DeathCondition {
    val conditions = listOf(
      EntityType.GHAST, EntityType.FIREBALL,
    )

    override val description: String = "to a Ghast"
    override val summary: String = "Die to Ghast"

    override fun test(cause: CauseOfDeath): Boolean =
      cause is VanillaMob &&
        conditions.contains(cause.entityType)

  }

  object MustBeRavager : DeathCondition {

    override val description: String = "to a Ravager"
    override val summary: String = "Die to Ravager"

    override fun test(cause: CauseOfDeath): Boolean =
      cause is VanillaMob &&
        cause.entityType == EntityType.RAVAGER

  }

  object MustBeIronGolem : DeathCondition {

    override val description: String = "to an Iron Golem"
    override val summary: String = "Die to Iron Golem"

    override fun test(cause: CauseOfDeath): Boolean =
      cause is VanillaMob &&
        cause.entityType == EntityType.IRON_GOLEM

  }

  object MustBeSilverfish : DeathCondition {

    override val description: String = "to Silverfish"
    override val summary: String = "Die to Silverfish"

    override fun test(cause: CauseOfDeath): Boolean =
      cause is VanillaMob &&
        cause.entityType == EntityType.SILVERFISH

  }

  object MustBeBlaze : DeathCondition {

    override val description: String = "to a Blaze"
    override val summary: String = "Die to Blaze"

    override fun test(cause: CauseOfDeath): Boolean =
      cause is VanillaMob &&
        cause.entityType == EntityType.BLAZE

  }

}
