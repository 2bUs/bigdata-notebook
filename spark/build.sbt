val sparkVersion = "1.6.0"
val kafkaVersion = "0.8.2.1"

val spark_mllib = "org.apache.spark" %% "spark-mllib" % sparkVersion % "provided"
val spark_core = "org.apache.spark" %% "spark-core" % sparkVersion % "provided"
val spark_csv = "com.databricks" %% "spark-csv" % "1.5.0" % "provided"
val spark_streaming = "org.apache.spark" %% "spark-streaming" % sparkVersion % "provided"  
val kafka_core = "org.apache.kafka" %% "kafka" % kafkaVersion
val flume_streaming = "org.apache.spark" %% "spark-streaming-flume" % sparkVersion  exclude("org.spark-project.spark", "unused")
val kafka_streaming = "org.apache.spark" %% "spark-streaming-kafka" % sparkVersion
val hbase_server = "org.apache.hbase" % "hbase-server" % "1.1.4" exclude("org.mortbay.jetty", "jsp-2.1")
val hbase_common  = "org.apache.hbase" % "hbase-common" % "1.1.4"
val spark_hive =  "org.apache.spark" %% "spark-hive" % sparkVersion exclude("com.twitter","parquet-hadoop-bundle")
val spark_mongodb_stratio = "com.stratio.datasource" %% "spark-mongodb" % "0.11.1"
val spark_avro = "com.databricks" %% "spark-avro" % "1.0.0"



name := "spark-vishnu"

lazy val commonSettings = Seq(
  organization := "com.vishnu",
  version := "1.0",
  scalaVersion := "2.10.5"
)

lazy val root = (project in file(".")).
  settings(commonSettings:_*).
  settings(
    name := "spark_examples",
    libraryDependencies += spark_mllib,
    libraryDependencies += spark_core,
    libraryDependencies += spark_csv,
    libraryDependencies += spark_streaming,
    libraryDependencies += flume_streaming,
    libraryDependencies += kafka_streaming,
    libraryDependencies += kafka_core,
    libraryDependencies += hbase_server,
    libraryDependencies += hbase_common,
    libraryDependencies += spark_hive,
    libraryDependencies += spark_mongodb_stratio,
    libraryDependencies += spark_avro,
    
    retrieveManaged := true
  )


val excludedFiles = Seq("pom.xml", "pom.properties", "manifest.mf", "package-info.class","plugin.xml")

mergeStrategy in assembly <<= (mergeStrategy in assembly) { (old) =>
  {
    case PathList("javax", "servlet", xs @ _*)         => MergeStrategy.first
    case PathList("jetty", "jsp", xs @ _*)         => MergeStrategy.first
    case x if x.contains("w3c") => MergeStrategy.first
    case x if x.contains("commons") => MergeStrategy.first
    case x if x.contains("javax/xml/") => MergeStrategy.first
    case x if x.contains("minlog") => MergeStrategy.first
    case x if x.contains("unused") => MergeStrategy.last
    case x if x.contains("html") => MergeStrategy.discard 
    case x if x.startsWith("com/google/common/base/") => MergeStrategy.first
    case "application.conf" => MergeStrategy.concat
    case x if excludedFiles.exists(x.endsWith(_)) => MergeStrategy.discard
    case x => old(x)
  }
}

