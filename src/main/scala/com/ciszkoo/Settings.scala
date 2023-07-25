package com.ciszkoo

import com.typesafe.config.Config

class Settings(config: Config) {
  val kafkaSettings = KafkaSettings(config.getString("kafka.kafka-server"), config.getString("kafka.kafka-topic"))
}

case class KafkaSettings(kafkaServer: String, kafkaTopic: String)
