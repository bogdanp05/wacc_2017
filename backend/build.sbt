import com.typesafe.sbt.packager.docker.{Cmd, ExecCmd}

name := "backend"
 
version := "1.0" 
      
lazy val `backend` = (project in file(".")).enablePlugins(PlayScala)

resolvers += "scalaz-bintray" at "https://dl.bintray.com/scalaz/releases"
      
resolvers += "Akka Snapshot Repository" at "http://repo.akka.io/snapshots/"

resolvers ++= Seq(
  Resolver.typesafeRepo("releases"),
  Resolver.sonatypeRepo("releases"),
  Resolver.sonatypeRepo("snapshots")
)
      
scalaVersion := "2.12.2"

libraryDependencies ++= Seq(
  jdbc ,
  ehcache ,
  ws ,
  guice,
  "org.reactivemongo" %% "play2-reactivemongo" % "0.12.6-play26",
  "org.scalatestplus.play" %% "scalatestplus-play" % "3.1.2" % "test",

  // connector of cassandra
  "com.datastax.cassandra" % "cassandra-driver-core" % "3.3.0",
  "com.datastax.cassandra" % "cassandra-driver-mapping" % "3.3.0",
  "com.datastax.cassandra" % "cassandra-driver-extras" % "3.3.0",
  "com.chuusai" %% "shapeless" % "2.3.2",
  "com.outworkers"  %% "phantom-dsl" % "2.14.5",

  //json
  "com.typesafe.play" %% "play-json-joda" % "2.6.6",
  "io.circe" %% "circe-core" % "0.8.0",
  "io.circe" %% "circe-generic" % "0.8.0",
  "io.circe" %% "circe-parser" % "0.8.0")

unmanagedResourceDirectories in Test <+=  baseDirectory ( _ /"target/web/public/test" )

dockerCommands := Seq(
  Cmd("FROM", "openjdk:latest"),
  Cmd("WORKDIR", "/opt/docker"),
  Cmd("ADD", "opt", "/opt"),
  Cmd("COPY", "dataset", "/var/lib/dataset"),
  Cmd("RUN", "[\"chown\", \"-R\", \"daemon:daemon\", \".\"]"),
  Cmd("USER", "daemon"),
  Cmd("ENTRYPOINT", "[\"bin/backend\"]"),
  ExecCmd("CMD")
)



      