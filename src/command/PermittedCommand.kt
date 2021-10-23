
package com.mercerenies.turtletroll.command

data class PermittedCommand<T: Command>(
  val command: T,
  val permission: String,
)

fun<T: Command> T.withPermission(permission: String): PermittedCommand<T> =
  PermittedCommand(this, permission)
