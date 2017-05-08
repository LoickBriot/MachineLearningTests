name := "spark"

/*** COMMON SETTINGS ***/
lazy val commonSettings = Seq(
  organization := "eu.tetrao",
  version := "0.1.1",
  scalaVersion := "2.11.8",
  scalacOptions ++= Seq("-unchecked", "-deprecation", "-feature", "-target:jvm-1.8")
)




lazy val regression  = (project in file("subprojects/machinelearning"))
  .settings(name := "spark-regression")
  .settings(commonSettings: _*)
  .settings(
    resolvers ++= Seq(
      Resolver.mavenLocal
    )
  )
  .settings(
    libraryDependencies ++= Seq(

      //SPARK LIBRARY
      "org.apache.hadoop" % "hadoop-client" % "2.7.3",
      "org.apache.spark" % "spark-core_2.11" % "2.0.0",
      "org.apache.spark" % "spark-mllib_2.11" % "2.0.0",
      "org.scalanlp" % "breeze_2.11" % "0.12",
      "org.scalanlp" % "breeze-natives_2.11" % "0.12",
      "org.scalanlp" % "breeze-viz_2.11" % "0.12",


      //DEEPLEARNING4J LIBRARY
      "org.deeplearning4j" % "deeplearning4j-core" % "0.8.0",
      "org.nd4j" % "nd4j-native-platform" % "0.8.0",


      //SMILE LIBRARY
      "com.github.haifengl" % "smile-core" % "1.3.1",
      "com.github.haifengl" % "smile-plot" % "1.3.1",
      "org.scala-lang.modules" %% "scala-swing" % "1.0.2",
      "com.github.tototoshi" %% "scala-csv" % "1.3.4"
    )
  )


/*** ROOT PROJECT ***/
lazy val spark = (project in file("."))
  .settings(commonSettings: _*)
  .aggregate(regression)


