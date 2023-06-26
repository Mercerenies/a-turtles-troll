
package com.mercerenies.turtletroll.config

interface ConfigOptions {

  fun<T> getValue(classType: Class<T>, path: String): T?

  fun getBoolean(path: String): Boolean? =
    getValue(Class.BOOLEAN, path)

  fun getInt(path: String): Int? =
    getValue(Class.INT, path)

  fun getDouble(path: String): Double? =
    getValue(Class.DOUBLE, path)

  fun getString(path: String): String? =
    getValue(Class.STRING, path)

}
