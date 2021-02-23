name := "financeProject"

version := "0.1"

scalaVersion := "2.12.12"

val appDependencies = Seq(
  "com.datastax.spark" %% "spark-cassandra-connector" % "3.0.0"
)

libraryDependencies++=Seq(
  "com.typesafe" % "config" %"1.3.4",
  "org.apache.spark" %% "spark-core" % "3.0.0",
  "org.apache.spark" %% "spark-sql" % "3.0.0",
  "org.apache.spark" %% "spark-mllib" % "3.0.0",
  "org.apache.spark" %% "spark-streaming" % "3.0.0",
  "org.apache.kafka" % "kafka-clients" % "2.7.0",
  "com.lihaoyi" %% "requests" % "0.6.5",
  "com.lihaoyi" %% "ujson" % "0.9.5",
  "joda-time" % "joda-time" % "2.3",
  "org.joda" % "joda-convert" % "1.6"


)

libraryDependencies += "log4j" % "log4j" % "1.2.14"
// https://mvnrepository.com/artifact/com.github.jnr/jnr-posix
libraryDependencies += "com.github.jnr" % "jnr-posix" % "3.0.61"
// https://mvnrepository.com/artifact/joda-time/joda-time
libraryDependencies += "joda-time" % "joda-time" % "2.10.6"
