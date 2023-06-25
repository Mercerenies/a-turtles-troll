
package com.mercerenies.turtletroll.config

interface ConfigOptions {

  fun<T> getValue(classType: Class<T>, path: String): T?

}
