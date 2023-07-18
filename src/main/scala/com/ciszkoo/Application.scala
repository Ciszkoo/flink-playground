package com.ciszkoo

import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment

object Application {
  def main(args: Array[String]): Unit = {
    Application().startPipeline()
  }
}

class Application() {
  def startPipeline(): Unit = {
    given StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment()

    Job().start()
  }
}
