name := """simple-rest-scala"""

version := "1.0-SNAPSHOT"

scalaVersion := "2.11.1"

libraryDependencies += "mysql" % "mysql-connector-java" % "5.1.27"

libraryDependencies += jdbc

libraryDependencies += filters

lazy val root = project.in(file(".")).enablePlugins(PlayScala)
