
package com.mercerenies.turtletroll.cake

enum class MealType {
  Cake,
  Cookie;

  val mealName: String
    get() =
      when (this) {
        Cake -> "cake"
        Cookie -> "cookie"
      }

}
