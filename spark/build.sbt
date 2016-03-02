val spark_mllib = "org.apache.spark" % "spark-mllib_2.10" % "1.5.2"
val spark_core = "org.apache.spark" % "spark-core_2.10" % "1.5.2"
val spark_csv = "com.databricks" % "spark-csv_2.10" % "1.2.0"
val spark_streaming = "org.apache.spark" % "spark-streaming_2.10" % "1.4.1"

lazy val commonSettings = Seq(
  organization := "com.vishnu",
  version := "0.1.0",
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
    retrieveManaged := true
  )

