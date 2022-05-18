
ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "2.13.8"

lazy val root = (project in file("."))
  .settings(
    name := "kafka-streams"
  )

libraryDependencies += "org.apache.kafka" % "kafka-streams" % "3.1.0" % "provided"

libraryDependencies += "org.slf4j" % "slf4j-api" % "1.7.36"
libraryDependencies += "org.slf4j" % "slf4j-reload4j" % "1.7.36"
enablePlugins(DockerPlugin)
ThisBuild / organization := "adam"
docker / dockerfile := {
  val jarFile: File = (Compile / packageBin / sbt.Keys.`package`).value
  val classpath = (Compile / managedClasspath).value
  val mainclass = (Compile / packageBin / mainClass).value.getOrElse(sys.error("Expected exactly one main class"))
  val jarTarget = s"/opt/kafka/jars/${jarFile.getName}"
  val classpathString = classpath.files.map("/opt/kafka/work-dir/" + _.getName)
    .mkString(":") + ":" + jarTarget
  ImageName(s"${organization.value}/${name.value}:latest")
  new Dockerfile {
    from("openjdk:11.0.15-oracle")
    add(jarFile, jarTarget)
    user("root")
    run(jarTarget)
    entryPoint("java", "-cp", classpathString, mainclass)
  }
}