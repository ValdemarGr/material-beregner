import sbt._

object Dependencies {
  lazy val scalaTest = "org.scalatest" %% "scalatest" % "3.0.5"
  lazy val akkaHttp = "com.typesafe.akka" %% "akka-http" % "10.1.8"
  lazy val akkaActor = "com.typesafe.akka" %% "akka-actor" % "2.6.0-M2"
  lazy val akkaStream = "com.typesafe.akka" %% "akka-stream" % "2.6.0-M2"
  lazy val guice = "com.google.inject" % "guice" % "4.2.2"
  lazy val circeGeneric = "io.circe" %% "circe-generic" % "0.12.0-M1"
  lazy val circeParser = "io.circe" %% "circe-parser" % "0.12.0-M1"
  lazy val circeCore = "io.circe" %% "circe-core" % "0.12.0-M1"
  lazy val scalaXml = "org.scala-lang.modules" %% "scala-xml" % "1.2.0"
  lazy val scalaTestKafka = "io.github.embeddedkafka" %% "embedded-kafka" % "2.3.0" % "test"
  lazy val akkaKafka = "com.typesafe.akka" %% "akka-stream-kafka" % "1.0.5"
  lazy val kafkaStreamsJava = "org.apache.kafka" % "kafka-streams" % "2.3.0"
  lazy val kafkaStreamsScala = "org.apache.kafka" %% "kafka-streams-scala" % "2.3.0"
  lazy val kafka = "org.apache.kafka" %% "kafka" % "2.3.0"
  lazy val zookeeper = "org.apache.zookeeper" % "zookeeper" % "3.5.5"
  lazy val slf4jlog = "org.slf4j" % "slf4j-log4j12" % "1.7.25"
  lazy val log4j = "log4j" % "log4j" % "1.2.17"
  lazy val playWs = "com.typesafe.play" %% "play-ahc-ws-standalone" % "2.1.0-M4"
  lazy val playWsXml = "com.typesafe.play" %% "play-ws-standalone-xml" % "2.1.0-M4"
  lazy val gcs = "com.google.cloud" % "google-cloud-storage" % "1.91.0"
  lazy val apacheCommons = "commons-io" % "commons-io" % "2.6"

  lazy val gcsNioForTesting = "com.google.cloud" % "google-cloud-nio" % "0.109.0-alpha"

  lazy val jobApi = "cerno" %% "cernoapiinternaljob" % "1.7.0-SNAPSHOT"
  lazy val publicApi = "cerno" %% "cernoprogrammaticapitypes" % "ee70179f27801767038d49e9b3e3ab0b8329182d"

  lazy val logback = "ch.qos.logback" % "logback-classic" % "1.2.3"
  lazy val playLogback = "com.typesafe.play" %% "play-logback" % "2.7.3"
  lazy val scalaLogging = "com.typesafe.scala-logging" %% "scala-logging" % "3.9.2"
  lazy val pprint = "com.lihaoyi" %% "pprint" % "0.5.6"
  lazy val sdis = "io.github.valdemargr" %% "sdis" % "1.2.3"

  lazy val catsCore = "org.typelevel" %% "cats-core" % "2.1.0"
  lazy val catsEffect = "org.typelevel" %% "cats-effect" % "2.1.0"

  lazy val fs2Core = "co.fs2" %% "fs2-core" % "2.2.1"
  lazy val fs2IO = "co.fs2" %% "fs2-io" % "2.2.1"
  
  lazy val redis4cats = "dev.profunktor" %% "redis4cats-effects" % "0.9.3"

  lazy val http4sCore = "org.http4s" %% "http4s-core" % "0.21.0"
  lazy val http4sDsl = "org.http4s" %% "http4s-dsl" % "0.21.0"
  lazy val http4sBlazeServer = "org.http4s" %% "http4s-blaze-server" % "0.21.0"
  lazy val http4sCirce = "org.http4s" %% "http4s-circe" % "0.21.0"

  lazy val fs2Kafka = "com.github.fd4s" %% "fs2-kafka" % "1.0.0"
}
