package com

import org.apache.flink.api.common.typeinfo.TypeInformation
import org.apache.flink.api.serializers.*

package object ciszkoo {
  case class UserClassified(ageGroup: AgeGroup, value: Int)
  object UserClassified {
    given userToClassified: Conversion[User, UserClassified] with
      def apply(x: User): UserClassified = {
        val value = if x.action == "logged_in" then 1 else -1
        UserClassified(x.age, value)
      }

    given TypeInformation[UserClassified] = deriveTypeInformation
  }

  sealed trait AgeGroup

  object AgeGroup {
    case object YoungAdult extends AgeGroup
    case object Adult extends AgeGroup
    case object Senior extends AgeGroup

    given intToAgeGroup: Conversion[Int, AgeGroup] with
      def apply(x: Int): AgeGroup = x match
        case x if x <= 25 => YoungAdult
        case x if x <= 60 => Adult
        case _            => Senior

    given TypeInformation[AgeGroup] = deriveTypeInformation
  }

  case class ActiveUsersChanges(var youngAdults: Int, var adults: Int, var seniors: Int) {
    override def toString(): String =
      s"ActiveUsersChanges(youngAdults = ${youngAdults}, adults = ${adults}, seniors = ${seniors})"
  }
  object ActiveUsersChanges {
    given TypeInformation[ActiveUsersChanges] = deriveTypeInformation
  }
}
