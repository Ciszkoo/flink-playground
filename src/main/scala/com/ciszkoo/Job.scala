package com.ciszkoo

import org.apache.flink.api.common.eventtime.WatermarkStrategy
import org.apache.flink.api.DataStream
import org.apache.flink.api.StreamExecutionEnvironment
import org.apache.flink.connector.kafka.source.reader.deserializer.KafkaRecordDeserializationSchema
import org.apache.flink.connector.kafka.source.KafkaSource

class Job()(using env: StreamExecutionEnvironment, settings: Settings) {
  def start(): Unit = env.execute("My Flink Job")

  val kafkaSource = KafkaSource
    .builder[User]()
    .setBootstrapServers(settings.kafkaSettings.kafkaServer)
    .setTopics(settings.kafkaSettings.kafkaTopic)
    .setDeserializer(KafkaRecordDeserializationSchema.of(UserDeserializer()))
    .build()

  val kafkaStream: DataStream[User] = env.fromSource(kafkaSource, WatermarkStrategy.noWatermarks(), "Kafka Source")

  val pipeline = Pipeline(kafkaStream).pipeline

  val sink = pipeline.print()
}
