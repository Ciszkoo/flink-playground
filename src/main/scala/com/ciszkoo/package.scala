package com

import org.apache.flink.api.common.typeinfo.TypeInformation
import org.apache.flink.api.serializers._

package object ciszkoo {
  case class TrafficByAgeGroup(youngAdults: Int, adults: Int, seniors: Int)

  object TrafficByAgeGroup {
    given trafficByAgeGroupTypeInfo: TypeInformation[TrafficByAgeGroup] = deriveTypeInformation
  }

  case class UserAgeClassified(user_id: String, name: String, age_group: AgeGroup, action: String)

  object UserAgeClassified {
    given userAgeClassifiedTypeInfo: TypeInformation[UserAgeClassified] = deriveTypeInformation
  }

  sealed trait AgeGroup

  object AgeGroup {
    case object YoungAdult extends AgeGroup
    case object Adult extends AgeGroup
    case object Senior extends AgeGroup
  }
}
