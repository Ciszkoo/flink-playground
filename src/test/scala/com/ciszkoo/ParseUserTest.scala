package com.ciszkoo

import scala.io.Source

import io.circe.parser.*

class ParseUserTest extends munit.FunSuite {
  test("Json parsing") {
    val json: String = Source.fromResource("record.json").getLines().mkString

    decode[User](json) match {
      case Left(error)   => fail("Failed to parse json", error)
      case Right(result) => assert(result == User("US_1", "Adam", 23, "logged_in"))
    }
  }
}
