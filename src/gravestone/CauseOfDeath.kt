
package com.mercerenies.turtletroll.gravestone

import com.mercerenies.turtletroll.util.component.*

import org.bukkit.event.entity.PlayerDeathEvent
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.entity.EntityType

import net.kyori.adventure.text.Component

import kotlin.text.Regex
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

sealed interface CauseOfDeath {
  abstract fun toInscription(): Component

  companion object {

    val BLINK_REGEX = Regex("""blinked\S*$""")
    val MIMIC_REGEX = Regex("""Mimic\S*$""")
    val OLDAGE_REGEX = Regex("""old age\S*$""")
    val FROZE_REGEX = Regex("""froze to death\S*$""")
    val DROWNED_REGEX = Regex("""drowned\S*$""")
    val COOKIE_REGEX = Regex("""ate a cookie""")
    val REDSTONE_REGEX = Regex("""strange dust""")
    val BIRCH_REGEX = Regex("""tree\S*$""")

    private fun getDeathMessage(event: PlayerDeathEvent): String =
      event.deathMessage()?.asPlainText() ?: ""

    fun identify(event: PlayerDeathEvent): CauseOfDeath {
      val cause = event.entity.getLastDamageCause()

      // TODO (HACK) Use the custom death message listener for this
      // rather than a bunch of hacked together regexes.

      // First, check for our custom stuff
      if (BLINK_REGEX.find(getDeathMessage(event)) != null) {
        return Angel
      }
      if (MIMIC_REGEX.find(getDeathMessage(event)) != null) {
        return Mimic
      }
      if (OLDAGE_REGEX.find(getDeathMessage(event)) != null) {
        return OldAge
      }
      if (FROZE_REGEX.find(getDeathMessage(event)) != null) {
        return Vanilla(EntityDamageEvent.DamageCause.FREEZE)
      }
      if (DROWNED_REGEX.find(getDeathMessage(event)) != null) {
        return Vanilla(EntityDamageEvent.DamageCause.DROWNING)
      }
      if (COOKIE_REGEX.find(getDeathMessage(event)) != null) {
        return Cookie
      }
      if (REDSTONE_REGEX.find(getDeathMessage(event)) != null) {
        return Redstone
      }
      if (BIRCH_REGEX.find(getDeathMessage(event)) != null) {
        return Birch
      }

      // Next, handle damage caused by an entity
      if ((cause is EntityDamageByEntityEvent) && (VanillaMob.handles(cause.getDamager().type))) {
        return VanillaMob(cause.getDamager().type)
      }

      // Not sure why the cause would be null, but Kotlin says it
      // might happen
      if (cause == null) {
        return Vanilla(EntityDamageEvent.DamageCause.CUSTOM)
      }

      // Otherwise, standard message
      return Vanilla(cause.getCause())

    }

    fun inscription(event: PlayerDeathEvent, timestamp: LocalDateTime): Inscriptions =
      CauseInscriptions(event, timestamp)

    fun inscription(event: PlayerDeathEvent): Inscriptions =
      inscription(event, LocalDateTime.now())

    private class CauseInscriptions(
      val event: PlayerDeathEvent,
      val timestamp: LocalDateTime,
    ) : Inscriptions {
      val cause: CauseOfDeath = identify(event)
      override fun getLine(index: Int): Component =
        when (index) {
          0 -> {
            // Player name
            event.entity.displayName()
          }
          1 -> {
            cause.toInscription()
          }
          2 -> {
            Component.text(timestamp.format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT)))
          }
          else -> {
            Component.text("")
          }
        }
    }

  }

}

// TODO These should be nested inside CauseOfDeath.

object Angel : CauseOfDeath {
  override fun toInscription(): Component =
    Component.text("Blinked")
}

object Mimic : CauseOfDeath {
  override fun toInscription(): Component =
    Component.text("Ouchie chest")
}

object OldAge : CauseOfDeath {
  override fun toInscription(): Component =
    Component.text("Got ooooold")
}

object Cookie : CauseOfDeath {
  override fun toInscription(): Component =
    Component.text("Death cookie")
}

object Redstone : CauseOfDeath {
  override fun toInscription(): Component =
    Component.text("Sussy dust")
}

object Birch : CauseOfDeath {
  override fun toInscription(): Component =
    Component.text("Trees with eyes")
}

data class Vanilla(val cause: EntityDamageEvent.DamageCause) : CauseOfDeath {

  override fun toInscription(): Component =
    Component.text(inscriptionText())

  private fun inscriptionText(): String =
    when (cause) {
      EntityDamageEvent.DamageCause.BLOCK_EXPLOSION -> "TNT go boom"
      EntityDamageEvent.DamageCause.CAMPFIRE -> "Hot wood"
      EntityDamageEvent.DamageCause.CONTACT -> "Prickly tree"
      EntityDamageEvent.DamageCause.CRAMMING -> "Overpopulation"
      EntityDamageEvent.DamageCause.CUSTOM -> "Idk"
      EntityDamageEvent.DamageCause.DRAGON_BREATH -> "Got breathed on"
      EntityDamageEvent.DamageCause.DROWNING, EntityDamageEvent.DamageCause.DRYOUT, EntityDamageEvent.DamageCause.SUFFOCATION -> "Forgot to breathe"
      EntityDamageEvent.DamageCause.ENTITY_ATTACK, EntityDamageEvent.DamageCause.ENTITY_SWEEP_ATTACK -> "Poked by bad guy"
      EntityDamageEvent.DamageCause.ENTITY_EXPLOSION -> "Sploded by bad guy"
      EntityDamageEvent.DamageCause.FALL -> "Gravity"
      EntityDamageEvent.DamageCause.FALLING_BLOCK -> "The sky is falling"
      EntityDamageEvent.DamageCause.FIRE, EntityDamageEvent.DamageCause.FIRE_TICK, EntityDamageEvent.DamageCause.HOT_FLOOR, EntityDamageEvent.DamageCause.LAVA -> "Hot stuff"
      EntityDamageEvent.DamageCause.FLY_INTO_WALL -> "Came outta nowhere"
      EntityDamageEvent.DamageCause.FREEZE -> "Brrrrrrr"
      EntityDamageEvent.DamageCause.LIGHTNING -> "Shocking, innit?"
      EntityDamageEvent.DamageCause.MAGIC -> "Bippity boppity boo"
      EntityDamageEvent.DamageCause.MELTING -> "Wtf how even?"
      EntityDamageEvent.DamageCause.POISON -> "Drugs"
      EntityDamageEvent.DamageCause.PROJECTILE -> "Hit by a rock"
      EntityDamageEvent.DamageCause.STARVATION -> "Forgot to eat"
      EntityDamageEvent.DamageCause.SUICIDE -> "Forgot to live"
      EntityDamageEvent.DamageCause.THORNS -> "Prickly bad guy"
      EntityDamageEvent.DamageCause.VOID -> "Oops! Negative Y"
      EntityDamageEvent.DamageCause.WITHER -> "Powerful drugs"
      EntityDamageEvent.DamageCause.SONIC_BOOM -> "BOOOOOOOOOOOM"
      EntityDamageEvent.DamageCause.KILL -> "Admin hammer"
      EntityDamageEvent.DamageCause.WORLD_BORDER -> "The border wall"
    }

}

data class VanillaMob(val entityType: EntityType) : CauseOfDeath {

  companion object {
    fun handles(entityType: EntityType): Boolean =
      VanillaMob(entityType).inscriptionText() != null
  }

  override fun toInscription(): Component =
    Component.text(inscriptionText() ?: "Poked to death")

  private fun inscriptionText(): String? =
    when (entityType) {
      EntityType.AXOLOTL -> "Salamander"
      EntityType.AREA_EFFECT_CLOUD -> "Big ball of smoke"
      EntityType.ARMOR_STAND -> "Dress up&hurt me"
      EntityType.ARROW, EntityType.TRIDENT -> "Sharp rock"
      EntityType.BEE -> "Seinfeld"
      EntityType.BLAZE, EntityType.MAGMA_CUBE -> "Flamey boi"
      EntityType.CAVE_SPIDER, EntityType.SPIDER -> "Wall crawler"
      EntityType.CREEPER -> "Splodey boi"
      EntityType.DONKEY, EntityType.MULE -> "Shrek's buddy"
      EntityType.DRAGON_FIREBALL, EntityType.SMALL_FIREBALL, EntityType.FIREBALL -> "Great balls of fire"
      EntityType.DROWNED, EntityType.ZOMBIE, EntityType.HUSK, EntityType.ZOMBIE_VILLAGER -> "Braaaaaains"
      EntityType.ELDER_GUARDIAN, EntityType.GUARDIAN -> "Mind games"
      EntityType.END_CRYSTAL -> "Shiny rock"
      EntityType.ENDER_DRAGON -> "Big Bird"
      EntityType.ENDER_PEARL -> "Flying rock"
      EntityType.ENDERMAN -> "Marble Hornets"
      EntityType.ENDERMITE -> "Tiny Hornets"
      EntityType.EVOKER, EntityType.EVOKER_FANGS, EntityType.VEX -> "Draw Four"
      EntityType.FALLING_BLOCK -> "The sky is falling"
      EntityType.FIREWORK_ROCKET -> "Murica!!!"
      EntityType.GHAST -> "Boooooooooo"
      EntityType.GIANT -> "Big braaaaains"
      EntityType.HOGLIN, EntityType.PIGLIN, EntityType.PIGLIN_BRUTE, EntityType.ZOGLIN, EntityType.ZOMBIFIED_PIGLIN, EntityType.PIG -> "Porkchop"
      EntityType.HORSE, EntityType.ZOMBIE_HORSE, EntityType.SKELETON_HORSE -> "Pale rider"
      EntityType.SKELETON, EntityType.STRAY, EntityType.WITHER_SKELETON -> "Spooky scary"
      EntityType.SHULKER, EntityType.SHULKER_BULLET -> "Bitey chest"
      EntityType.SPLASH_POTION, EntityType.LINGERING_POTION -> "Magic sauce"
      EntityType.SILVERFISH -> "Shiny bug"
      EntityType.SPECTRAL_ARROW -> "Shiny rock"
      EntityType.ILLUSIONER -> "Now you see him"
      EntityType.IRON_GOLEM -> "Nerves of steel"
      EntityType.LIGHTNING_BOLT -> "Divine intervention"
      EntityType.LLAMA, EntityType.LLAMA_SPIT, EntityType.TRADER_LLAMA, EntityType.CAMEL -> "Kuzco's rage"
      EntityType.MINECART, EntityType.CHEST_MINECART, EntityType.FURNACE_MINECART, EntityType.COMMAND_BLOCK_MINECART, EntityType.HOPPER_MINECART, EntityType.SPAWNER_MINECART -> "Rollercoaster"
      EntityType.TNT_MINECART -> "Boomycoaster"
      EntityType.PARROT -> "Polly wanna cracker"
      EntityType.PHANTOM -> "Direction&magnitude"
      EntityType.POLAR_BEAR, EntityType.PANDA -> "Mama bear"
      EntityType.PUFFERFISH -> "Tears of the sky"
      EntityType.RABBIT -> "Holy hand grenade"
      EntityType.RAVAGER -> "Zoinks!"
      EntityType.SLIME -> "Slimer"
      EntityType.TNT -> "TNT go boom"
      EntityType.VINDICATOR, EntityType.PILLAGER -> "Raid shadow legends"
      EntityType.WARDEN -> "Daredevil"
      EntityType.WITCH -> "Bippity boppity boo"
      EntityType.WITHER, EntityType.WITHER_SKULL -> "Oof good luck with that"
      EntityType.WOLF -> "Man's best friend"
      // Entities that *shouldn't* be able to damage you
      else -> null
    }

}
