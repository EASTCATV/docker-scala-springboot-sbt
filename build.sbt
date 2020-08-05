name := "docker-scala-springboot-sbt"
version := "1.0.1"

startYear := Some(2020)
scalaVersion := "2.11.7"

scalacOptions ++= Seq(
  "-encoding", "utf8",
  "-unchecked",
  "-feature",
  "-deprecation"
)

javacOptions ++= Seq(
  "-source", "1.8",
  "-target", "1.8",
  "-encoding", "utf8",
  "-Xlint:unchecked",
  "-Xlint:deprecation",
  "-g"
)

val verSpringBoot = "2.0.0.RELEASE"

val verJackson = "2.9.4"
mainClass in(Compile, run) := Some("demo.Application")
libraryDependencies ++= Seq(
  "org.springframework.boot" % "spring-boot-starter" % verSpringBoot,
  "org.springframework.boot" % "spring-boot-starter-parent" % verSpringBoot,
  "org.springframework.boot" % "spring-boot-starter-web" % verSpringBoot,
//  "org.springframework.boot" % "spring-boot-starter-data-jpa" % verSpringBoot,
  "org.springframework.boot" % "spring-boot-devtools" % verSpringBoot,
  "org.springframework.boot" % "spring-boot-starter-actuator" % verSpringBoot,
  "org.springframework.boot" % "spring-boot-starter-tomcat" % verSpringBoot,
  "org.jolokia" % "jolokia-core" % "1.3.6",
//  "com.h2database" % "h2" % "1.4.193",
//  "org.hibernate" % "hibernate-java8" % "5.2.16.Final",
//  "mysql" % "mysql-connector-java" % "5.1.42",
  "commons-collections" % "commons-collections" % "3.1",
  "commons-lang" % "commons-lang" % "2.6",
  "commons-io" % "commons-io" % "2.5",
  "org.apache.httpcomponents" % "httpclient" % "4.3.1",
  "org.apache.httpcomponents" % "httpmime" % "4.5",
  "org.scalaj" %% "scalaj-http" % "2.3.0",
  "joda-time" % "joda-time" % "2.9.8",
  "com.fasterxml.jackson.dataformat" % "jackson-dataformat-xml" % verJackson,
  "com.fasterxml.jackson.datatype" % "jackson-datatype-jsr310" % verJackson,
  "com.fasterxml.jackson.module" %% "jackson-module-scala" % verJackson,
  "org.json4s" % "json4s-jackson_2.11" % "3.2.11",
  "org.json" % "json" % "20140107",
  "org.apache.logging.log4j" % "log4j-flume-ng" % "2.7",
  "com.github.docker-java" % "docker-java" % "3.0.7",
  "org.scalatest" % "scalatest_2.11" % "3.0.4" % "test",
  "com.opencsv" % "opencsv" % "4.1",
  "com.voodoodyne.jackson.jsog" % "jackson-jsog" % "1.1",
  "org.springframework" % "spring-websocket" % "4.1.9.RELEASE",
  "org.springframework" % "spring-messaging" % "4.1.9.RELEASE",
  "net.sf.json-lib" % "json-lib" % "2.4" from "http://repo1.maven.org/maven2/net/sf/json-lib/json-lib/2.4/json-lib-2.4-jdk15.jar",
  "net.sf.ezmorph" % "ezmorph" % "1.0.6"
)


enablePlugins(TomcatPlugin)
enablePlugins(sbtdocker.DockerPlugin)

dockerfile in docker := {
  var warFile = sbt.Keys.`package`.in(Compile, packageBin).value.toString().replaceAll(".jar", ".war")
  var tomcatConfig = sourceDirectory(_ / "main" / "webapp" / "server.xml").value.toString
  var catalina=sourceDirectory(_ / "main" / "webapp" / "catalina.sh").value.toString

  new Dockerfile {
    from("tomcat:8.5.16-jre8")
    env("TZ","Asia/Shanghai")
    copy(file(warFile), "/usr/local/tomcat/webapps/do.war")
    copy(file(tomcatConfig), "/usr/local/tomcat/conf/server.xml")
    run("ln" ,"-sf" ,"/usr/share/zoneinfo/Asia/Shanghai" ,"/etc/localtime")
    entryPoint("catalina.sh", "run")
  }
}

imageNames in docker := Seq(
  ImageName(namespace = Some("xxx.xxx.xxx"),
    repository = "xxx",
    tag = Some(version.value))
  //  ,
  //  ImageName(namespace = Some("xxx.xxx.xxx"),
  //    repository = "xxx",
  //    tag = Some("latest"))
)
resolvers += "Spring Releases" at "https://repo.spring.io/libs-release-local"
resolvers += "Spring Snapshots" at "https://repo.spring.io/libs-snapshot-local"
resolvers += "spring-milestones" at "https://repo.spring.io/libs-milestone-local"
resolvers += "spring-Releases" at "https://repo.spring.io/release"
