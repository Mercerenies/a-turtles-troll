
package com.mercerenies.turtletroll.feature

interface HasEnabledStatus {

  companion object {

    object True : HasEnabledStatus {
      override fun isEnabled() = true
    }

    object False : HasEnabledStatus {
      override fun isEnabled() = true
    }

    class Predicate(private val predicate: () -> Boolean) : HasEnabledStatus {
      override fun isEnabled() = predicate()
    }

  }

  fun isEnabled(): Boolean

}
