import sbt.Process

import java.util.concurrent.atomic.AtomicReference

name := "book Demo project"
version := "1.0.0"

scalaVersion := "2.11.8"
scalacOptions in ThisBuild ++= Seq("-feature", "-target:jvm-1.8")
scalacOptions in Compile ++= Seq("-Xlint", "-Ywarn-unused", "-Ywarn-unused-import")
publishMavenStyle := false

lazy val start = taskKey("tomcat start")
lazy val stop = taskKey("tomcat stop")
lazy val tomcat = config("tomcat").hide


lazy val root = (project in file("."))
  .settings(
    name := "BookDemoProject"
  )

libraryDependencies ++= Seq(
  "com.fasterxml.jackson.module" %% "jackson-module-scala" % "2.12.5",
  "javax.servlet" % "javax.servlet-api" % "4.0.0" % "provided",
  "com.oracle.database.jdbc" % "ojdbc8" % "19.8.0.0",
  "com.nylas.sdk" % "nylas-java-sdk" % "1.11.2",
  "org.apache.cassandra" % "cassandra-all" % "4.0.3",
  "org.projectlombok" % "lombok" % "1.18.20" % "provided",
  "com.google.code.gson" % "gson" % "2.8.5",
  "com.github.jsimone" % "webapp-runner" % "9.0.27.1" intransitive()
)


lazy val tomcatInstance = settingKey[AtomicReference[Option[Process]]]("current container process")

tomcatInstance := new AtomicReference(None)

def stopProcess(l: Logger)(p: Process) {
  p.destroy()
  val err = System.err
  val devNull: java.io.PrintStream =
    new java.io.PrintStream(
      new java.io.OutputStream {
        def write(b: Int): Unit = {}
      }
    )
  System.setErr(devNull)
  p.exitValue()
  System.setErr(err)
}

def tomcatStop(l: Logger, atomicRef: AtomicReference[Option[Process]]): Unit = {
  val oldProcess = atomicRef.getAndSet(None)
  oldProcess.foreach(stopProcess(l))
}

onLoad in Global := (onLoad in Global).value andThen { state =>
  state.addExitHook(tomcatStop(state.log, tomcatInstance.value))
}

def launch = Def.task {
  val log = streams.value.log
  val instance = tomcatInstance.value

  tomcatStop(log, instance)

  val libs: Seq[File] = Seq((artifactPath in Compile in packageBin).value)   ++
    (fullClasspath in Runtime).value.map(_.data).filter(_.getName contains ".jar")

  val paths = Path.makeString(libs)

  val args = javaOptions.value ++
    Seq("-cp", paths) ++
    Seq("webapp.runner.launch.Main") ++
    Seq("--port", "9090") ++
    Seq((baseDirectory.value / "web").absolutePath)

  val process = new Fork("java", None).fork(ForkOptions(), args)
  instance.set(Some(process))
}

inConfig(tomcat)(Seq(
  start := (launch dependsOn(packageBin in Compile)).value
))
