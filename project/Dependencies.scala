import sbt.*

object Versions {
  val munit = "0.7.29"
  val flink = "1.17.1"
  val flinkScala = "1.17.1_1.0.0"
  val circe = "0.14.5"
  val scalaLogging = "3.9.4"
  val typesafeConfig = "1.4.2"
  val logback = "1.2.10"
}

object Dependencies {
  lazy val circe = Seq(
    "io.circe" %% "circe-core",
    "io.circe" %% "circe-generic",
    "io.circe" %% "circe-parser"
  ).map(_ % Versions.circe)

  lazy val flinkScalaApi = Seq(
    ("org.flinkextended" %% "flink-scala-api" % Versions.flinkScala)
      .exclude("org.scalameta", "mdoc_3")
  )

  lazy val flink = Seq(
    "org.apache.flink" % "flink-java",
    "org.apache.flink" % "flink-streaming-java",
    "org.apache.flink" % "flink-connector-kafka"
  ).map(_ % Versions.flink)

  lazy val deps = circe ++ flink ++ flinkScalaApi ++ Seq(
    "org.scalameta" %% "munit" % Versions.munit % "test",
    "com.typesafe.scala-logging" %% "scala-logging" % Versions.scalaLogging,
    "ch.qos.logback" % "logback-classic" % Versions.logback,
    "com.typesafe" % "config" % Versions.typesafeConfig
  )
}
