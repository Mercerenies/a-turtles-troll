
package com.mercerenies.turtletroll.config

// ConfigOptions but asserts non-null for all values. This should be
// used after defaults have been provided, as any null values at that
// point indicate a malformed config file.
class CheckedConfigOptions(
  private val impl: ConfigOptions,
) : ConfigOptions {

  override fun<T> getValue(classType: Class<T>, path: String): T {
    val result = impl.getValue(classType, path)
    if (result == null) {
      throw CheckedConfigAssertionException("Could not read $path from config, expecting ${classType.name}")
    }
    return result
  }

  override fun getBoolean(path: String): Boolean =
    getValue(Class.BOOLEAN, path)

  override fun getInt(path: String): Int =
    getValue(Class.INT, path)

  override fun getDouble(path: String): Double =
    getValue(Class.DOUBLE, path)

  override fun getString(path: String): String =
    getValue(Class.STRING, path)

}
