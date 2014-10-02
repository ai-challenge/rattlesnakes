name := "rattlesnakes-server"

version := "0.0-SNAPSHOT"

scalaVersion := "2.11.2"

libraryDependencies ++= Seq(
  jdbc,
  "com.lucidchart" %% "relate" % "1.6.0",
  "org.xerial" % "sqlite-jdbc" % "3.7.2"
)

lazy val root = (project in file(".")).enablePlugins(PlayScala)
