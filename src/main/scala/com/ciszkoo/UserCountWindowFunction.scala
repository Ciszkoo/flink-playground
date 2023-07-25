package com.ciszkoo

import java.lang

import com.ciszkoo.TrafficByAgeGroup
import org.apache.flink.api.function.ProcessAllWindowFunction
import org.apache.flink.streaming.api.windowing.windows.TimeWindow
import org.apache.flink.util.Collector

class UserCountWindowFunction() extends ProcessAllWindowFunction[UserAgeClassified, TrafficByAgeGroup, TimeWindow] {
  def process(context: Context, elements: Iterable[UserAgeClassified], out: Collector[TrafficByAgeGroup]): Unit = {
    var youngAdults = 0
    var adults = 0
    var seniors = 0

    elements.foreach { user =>
      user match
        case UserAgeClassified(_, _, age_group, action) if action == "logged_in" =>
          age_group match
            case AgeGroup.YoungAdult => youngAdults += 1
            case AgeGroup.Adult      => adults += 1
            case AgeGroup.Senior     => seniors += 1
        case UserAgeClassified(_, _, age_group, action) if action == "logged_out" =>
          age_group match
            case AgeGroup.YoungAdult => youngAdults -= 1
            case AgeGroup.Adult      => adults -= 1
            case AgeGroup.Senior     => seniors -= 1
        case UserAgeClassified(user_id, name, age_group, action) => ???
    }

    out.collect(TrafficByAgeGroup(youngAdults, adults, seniors))
  }
}
