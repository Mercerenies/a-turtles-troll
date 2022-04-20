
package com.mercerenies.turtletroll.storage

import kotlin.collections.HashMap

interface GlobalDataHolder {

  fun getData(key: String): String?

  fun putData(key: String, value: String)

}
