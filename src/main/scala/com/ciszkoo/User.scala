package com.ciszkoo

import io.circe._
import io.circe.parser._

case class User(id: String, name: String, age: Int, action: String)

object User {
  def empty: User = User("", "", 0, "")

  given Decoder[User] = Decoder[User] { cursor =>
    for {
      id <- cursor.downField("user_id").as[String]
      name <- cursor.downField("name").as[String]
      age <- cursor.downField("age").as[Int]
      action <- cursor.downField("action").as[String]
    } yield User(id, name, age, action)
  }
}
