package com.ciszkoo

import org.apache.flink.api.common.functions.MapFunction

class LetterCounter() extends MapFunction[String, Int] {
  def map(value: String): Int = value.count(_.isLetter)
}
