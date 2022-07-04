ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "2.13.8"

lazy val root = (project in file("."))
  .settings(
    name := "BookDemoProject"
  )

libraryDependencies ++= Seq(
"com.fasterxml.jackson.module" %% "jackson-module-scala" % "2.12.5",
  "javax.servlet" % "javax.servlet-api" % "4.0.0" % "provided",
  "com.oracle.database.jdbc" % "ojdbc8" % "19.8.0.0",
  "com.oracle.database.jdbc" % "ucp" % "19.8.0.0",
  "com.oracle.ojdbc" % "orai18n" % "19.3.0.0"
)
