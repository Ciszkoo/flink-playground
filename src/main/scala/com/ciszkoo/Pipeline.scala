package com.ciszkoo

import com.ciszkoo.AgeGroup.{Adult, Senior, YoungAdult}
import org.apache.flink.api.common.functions.{FilterFunction, MapFunction, ReduceFunction}
import org.apache.flink.api.function.ProcessAllWindowFunction
import org.apache.flink.api.DataStream
import org.apache.flink.streaming.api.windowing.assigners.{
  ProcessingTimeSessionWindows,
  TumblingProcessingTimeWindows
}
import org.apache.flink.streaming.api.windowing.time.Time
import org.apache.flink.streaming.api.windowing.windows.TimeWindow
import org.apache.flink.util.Collector

class Pipeline(stream: DataStream[User]) {
  val keyedPipeline = stream
    .filter(UserFilter())
    .map(UserClassifier())
    .keyBy(_.ageGroup)
    .window(TumblingProcessingTimeWindows.of(Time.minutes(1)))
    .reduce(UserReduceFunction())
    .windowAll(ProcessingTimeSessionWindows.withGap(Time.seconds(30)))
    .process(MergeActiveUsersChanges())
}

class UserClassifier() extends MapFunction[User, UserClassified] {
  def map(value: User): UserClassified = value
}

class UserFilter() extends FilterFunction[User] {
  def filter(value: User): Boolean = value.age >= 18 && (value.action == "logged_in" || value.action == "logged_out")
}

class UserReduceFunction() extends ReduceFunction[UserClassified] {
  def reduce(value1: UserClassified, value2: UserClassified): UserClassified = {
    val activeUsersChange = value1.value + value2.value

    value1.copy(value = activeUsersChange)
  }
}

class MergeActiveUsersChanges() extends ProcessAllWindowFunction[UserClassified, ActiveUsersChanges, TimeWindow] {
  def process(context: Context, elements: Iterable[UserClassified], out: Collector[ActiveUsersChanges]): Unit = {
    val activeUserChanges = ActiveUsersChanges(0, 0, 0)

    elements.foreach { e =>
      e.ageGroup match
        case YoungAdult => activeUserChanges.youngAdults = e.value
        case Adult      => activeUserChanges.adults = e.value
        case Senior     => activeUserChanges.seniors = e.value
    }

    out.collect(activeUserChanges)
  }
}
