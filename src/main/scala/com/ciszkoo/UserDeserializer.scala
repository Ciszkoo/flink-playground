package com.ciszkoo

import java.nio.charset.StandardCharsets

import com.typesafe.scalalogging.LazyLogging
import io.circe.parser.*
import org.apache.flink.api.common.typeinfo.TypeInformation
import org.apache.flink.api.java.typeutils.TypeExtractor
import org.apache.flink.streaming.connectors.kafka.KafkaDeserializationSchema
import org.apache.kafka.clients.consumer.ConsumerRecord

class UserDeserializer() extends KafkaDeserializationSchema[User] with LazyLogging {
  def deserialize(record: ConsumerRecord[Array[Byte], Array[Byte]]): User = {
    val msg = String(record.value(), StandardCharsets.UTF_8)

    decode[User](msg) match {
      case Left(value) => User.empty
      case Right(user) => {
        logger.info(s"User deserialized: ${user}")
        user
      }
    }
  }

  def isEndOfStream(nextElement: User): Boolean = false

  def getProducedType(): TypeInformation[User] = TypeExtractor.getForClass(classOf[User])
}
