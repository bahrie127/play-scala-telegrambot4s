name := """play-scala-telegrambot4s"""
organization := "pme"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.12.4"

libraryDependencies += ws
libraryDependencies += guice
libraryDependencies += "info.mukel" %% "telegrambot4s" % "3.0.14"

libraryDependencies += "org.scalatestplus.play" %% "scalatestplus-play" % "3.1.2" % Test

libraryDependencies ++= Seq(
    "org.mariadb.jdbc" % "mariadb-java-client" % "1.2.3",
    "com.typesafe.slick" %% "slick-hikaricp" % "3.2.3",
    "com.typesafe.slick" %% "slick" % "3.2.3",
    "com.typesafe.slick" %% "slick-codegen" % "3.2.3",
    "com.linecorp.bot" % "line-bot-api-client" % "1.6.0",
    "com.linecorp.bot" % "line-bot-model" % "1.6.0",
    "com.squareup.okhttp3" % "okhttp" % "3.6.0"
)

// Adds additional packages into Twirl
//TwirlKeys.templateImports += "pme.controllers._"

// Adds additional packages into conf/routes
// play.sbt.routes.RoutesKeys.routesImport += "pme.binders._"
