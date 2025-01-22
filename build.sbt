name := """student-test"""
organization := "com.example"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.13.16"

libraryDependencies += guice
libraryDependencies += "org.scalatestplus.play" %% "scalatestplus-play" % "6.0.0" % Test


libraryDependencies ++= Seq(
  jdbc,
  ws,
  "org.postgresql" % "postgresql" % "42.7.3",
  "org.playframework.anorm" %% "anorm" % "2.7.0",
  "org.flywaydb" %% "flyway-play" % "9.1.0"
)

