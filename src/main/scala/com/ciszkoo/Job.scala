package com.ciszkoo

// import org.apache.flink.api.common.serialization.SimpleStringSchema
import org.apache.flink.connector.kafka.source.KafkaSource
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment
import org.apache.flink.api.common.eventtime.WatermarkStrategy
import org.apache.flink.connector.kafka.source.reader.deserializer.KafkaRecordDeserializationSchema

class Job()(using env: StreamExecutionEnvironment) {
  def start(): Unit = env.execute("My Flink Job")

  val kafkaSource = KafkaSource
    .builder[User]()
    .setBootstrapServers("localhost:9092")
    .setTopics("users")
    // .setValueOnlyDeserializer(new SimpleStringSchema)
    .setDeserializer(KafkaRecordDeserializationSchema.of(UserDeserializer()))
    .build()

  val kafkaStream = env.fromSource(kafkaSource, WatermarkStrategy.noWatermarks(), "Kafka Source")

  val sink = kafkaStream.print()
}
