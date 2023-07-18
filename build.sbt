// ThisBuild / resolvers ++= Seq(
//     "Apache Development Snapshot Repository" at "https://repository.apache.org/content/repositories/snapshots/",
//     Resolver.mavenLocal
// )

name := "flink-playground"
version := "0.1.0-SNAPSHOT"
organization := "com.ciszko"

ThisBuild / scalaVersion := "3.3.0"

lazy val root = project
  .in(file("."))
  .settings(
    libraryDependencies ++= flinkDependencies ++ circeDependencies,
    libraryDependencies += "org.scalameta" %% "munit" % "0.7.29" % Test,
  )

lazy val flinkVersion = "1.17.1"

lazy val flinkDependencies = Seq(
  "org.apache.flink" % "flink-java" % flinkVersion % "provided",
  "org.apache.flink" % "flink-streaming-java" % flinkVersion % "provided",
  "org.apache.flink" % "flink-connector-kafka" % flinkVersion,
)

lazy val circeVersion = "0.14.5"

lazy val circeDependencies = Seq(
  "io.circe" %% "circe-core" % circeVersion,
  "io.circe" %% "circe-generic" % circeVersion,
  "io.circe" %% "circe-parser" % circeVersion,
)

// ThisBuild / assemblyMergeStrategy := {
//   case PathList("META-INF", _*) => MergeStrategy.discard
//   case _: String                => MergeStrategy.first
// }

assembly / mainClass := Some("com.ciszkoo.Application")

Compile / run := Defaults
  .runTask(Compile / fullClasspath, Compile / run / mainClass, Compile / run / runner)
  .evaluated

Compile / run / fork := true
Global / cancelable := true

assembly / assemblyOption ~= {
  _.withIncludeScala(true)
}
