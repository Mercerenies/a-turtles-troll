
package com.mercerenies.turtletroll.nms

import org.bukkit.Bukkit
import org.bukkit.entity.FallingBlock

// https://riptutorial.com/bukkit/example/29589/accessing-the-current-minecraft-version :)
object NMS {

  fun version(): String =
    Bukkit.getServer()::class.java.getPackage().getName().substring(23)

  fun getClass(suffix: String): Class<*> =
    Class.forName("org.bukkit.craftbukkit.${version()}.${suffix}")

  fun makeFallingBlockHurt(block: FallingBlock) {
    val cls = getClass("entity.CraftFallingBlock")
    val mcBlock = cls.getMethod("getHandle").invoke(block)
    Class.forName("net.minecraft.world.entity.item.EntityFallingBlock")
      .getMethod("b", java.lang.Float.TYPE, java.lang.Integer.TYPE)
      .invoke(mcBlock, 1.0f, 40)
  }

}
