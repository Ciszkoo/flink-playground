package com.ciszkoo

import org.apache.flink.api.StreamExecutionEnvironment
import com.typesafe.scalalogging.StrictLogging
import com.typesafe.config.ConfigFactory

object Application {
  def main(args: Array[String]): Unit = {
    val settings = Settings(ConfigFactory.load())

    given Settings = settings

    Application().startPipeline()
  }
}

class Application()(using settings: Settings) extends StrictLogging {
  def startPipeline(): Unit = {
    logger.info("Starting Flink job")
    given StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment

    Job().start()
  }
}
