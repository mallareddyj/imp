name := "DummyMojo"

version := "0.1"

scalaVersion := "2.13.4"

val mongodbV = "2.9.0"
val mongodb  = "org.mongodb.scala"    %% "mongo-scala-driver" % mongodbV
libraryDependencies ++=Seq(
  mongodb,
  "com.typesafe.akka" %% "akka-actor" % "2.6.10",
  "com.typesafe.akka" %% "akka-stream" % "2.6.10",
  "com.typesafe.akka" %% "akka-http" % "10.2.2",
  "com.typesafe.akka" %% "akka-http-spray-json" % "10.2.2",
  "com.typesafe.akka" %% "akka-http-testkit" % "10.2.2" % "test",
  "com.typesafe.akka" %% "akka-testkit" % "2.6.10" % Test,
  "com.typesafe.scala-logging" %% "scala-logging" % "3.9.2",
  "ch.qos.logback" % "logback-classic" % "1.2.3",
  "org.scalatest" %% "scalatest" % "3.0.8" % Test ,
  "org.mockito" % "mockito-core" % "2.23.4" % "test"
//  ,
//  "io.estatico" %% "bson-codec" % "0.0.1"
)