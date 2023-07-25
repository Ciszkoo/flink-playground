name := "flink-playground"
version := "0.1.0-SNAPSHOT"
organization := "com.ciszko"

ThisBuild / scalaVersion := "3.3.0"

lazy val root = project
  .in(file("."))
  .settings(
    assemblySettings,
    javacOptions ++= Seq("-source", "11", "-target", "11"),
    libraryDependencies ++= Dependencies.deps,
    Compile / run := Defaults
      .runTask(Compile / fullClasspath, Compile / run / mainClass, Compile / run / runner)
      .evaluated
  )

Compile / run / fork := true
Global / cancelable := true

lazy val assemblySettings = Seq(
  assembly / test := {},
  assemblyPackageScala / assembleArtifact := false,
  assembly / mainClass := Some("com.ciszkoo.Application"),
  assembly / assemblyOption ~= { _.withIncludeScala(true) },
  assembly / assemblyMergeStrategy := {
    case "application.conf" => MergeStrategy.concat
    case x: String if x.endsWith("io.netty.versions.properties") =>
      MergeStrategy.first
    case PathList("META-INF", "MANIFEST.MF") => MergeStrategy.discard
    case _: String =>
      MergeStrategy.first // MergeStrategy.defaultMergeStrategy(other)
  }
  // ThisBuild / assemblyMergeStrategy := {
  //   case PathList("META-INF", _*) => MergeStrategy.discard
  //   case _: String                => MergeStrategy.first
  // }
)