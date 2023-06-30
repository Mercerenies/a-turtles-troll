
package com.mercerenies.turtletroll.skin

import com.mojang.authlib.GameProfile
import com.mojang.authlib.properties.Property

object PlayerInfoPacketUpdater {

  fun replaceSkinTexture(gameProfile: GameProfile, newSkinString: String) {
    val newProperties = listOf(Property("textures", newSkinString))
    gameProfile.getProperties().replaceValues("textures", newProperties)
  }

}
