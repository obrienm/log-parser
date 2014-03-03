import sbt._
import Keys._

object ApplicationBuild extends Build {

  lazy val defaultSettings = Defaults.defaultSettings ++ Seq(
    version := "1.0",
    scalaVersion := "2.10.3",
    scalacOptions := Seq(
      "-feature",
      "-language:implicitConversions",
      "-language:postfixOps",
      "-unchecked",
      "-deprecation",
      "-encoding", "utf8",
      "-Ywarn-adapted-args"
    ),
    organization := "me.moschops"
  )

  val dependencies = Seq(
    "joda-time" % "joda-time" % "2.0",
    "org.joda" % "joda-convert" % "1.2",
    "commons-io" % "commons-io" % "2.1",
    "commons-lang" % "commons-lang" % "2.5",
    "commons-codec" % "commons-codec" % "1.9",
   "com.typesafe.akka" % "akka-agent_2.10" % "2.2.1",
    "ch.qos.logback" % "logback-classic" % "1.1.1",
    "ch.qos.logback" % "logback-core" % "1.1.1",
    "org.slf4j" % "slf4j-api" % "1.7.6"
  )
  
  lazy val root = Project(id = "LogParser",
    base = file(".")).settings(libraryDependencies ++= dependencies)
}
