name := "getting_started"

version := "1.0.0"

scalaVersion := "2.11.6"

scalacOptions ++= Seq("-feature", "-language:higherKinds")

libraryDependencies ++= Seq(
  "io.concord" % "concord" % "0.1.0",
  "io.concord" % "rawapi" % "0.1.0",
  "org.apache.kafka" % "kafka_2.11" % "0.8.2.1",
  "org.apache.kafka" % "kafka-clients" % "0.8.2.1"
)


assemblyMergeStrategy in assembly := {
  case x if x.endsWith("project.clj") => MergeStrategy.discard // Leiningen build files
  case x if x.toLowerCase.startsWith("meta-inf") => MergeStrategy.discard // More bumf
  case _ => MergeStrategy.first
}


resolvers += Resolver.sonatypeRepo("public")

resolvers += "clojars" at "https://clojars.org/repo"

resolvers += "conjars" at "http://conjars.org/repo"

resolvers += "Apache repo" at "https://repository.apache.org/content/repositories/releases"
