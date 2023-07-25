package com.ciszkoo

import org.apache.flink.api.common.functions.FilterFunction
import org.apache.flink.api.common.functions.MapFunction
import org.apache.flink.api.DataStream
import org.apache.flink.streaming.api.windowing.assigners.TumblingProcessingTimeWindows
import org.apache.flink.streaming.api.windowing.time.Time

class Pipeline(stream: DataStream[User]) {
  val pipeline = stream
    .filter(UserFilter())
    .map(UserAgeClassifier())
    .windowAll(TumblingProcessingTimeWindows.of(Time.minutes(1)))
    .process(UserCountWindowFunction())
}

class UserAgeClassifier() extends MapFunction[User, UserAgeClassified] {
  def map(value: User): UserAgeClassified = value.age match {
    case age if age <= 25 => UserAgeClassified(value.user_id, value.name, AgeGroup.YoungAdult, value.action)
    case age if age <= 60 => UserAgeClassified(value.user_id, value.name, AgeGroup.Adult, value.action)
    case _: Int           => UserAgeClassified(value.user_id, value.name, AgeGroup.Senior, value.action)
  }
}

class UserFilter() extends FilterFunction[User] {
  def filter(value: User): Boolean = value.age >= 18 && (value.action == "logged_in" || value.action == "logged_out")
}
