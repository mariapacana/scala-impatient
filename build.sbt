ThisBuild / scalaVersion := "2.12.8"

ThisBuild / organization := "com.example"

fork := true

scalacOptions ++= Seq("-unchecked", "-deprecation", "-feature")

libraryDependencies ++= Seq(
  "org.scalatest" %% "scalatest" % "3.0.5" % Test,
//  "org.scala-lang.modules" %% "scala-xml" % "2.0.0",
  "org.scala-lang.modules" %% "scala-parser-combinators" % "1.1.2",
  "org.scala-lang" % "scala-actors" % "2.11.5",
  "junit" % "junit" % "4.11",
  "org.hamcrest" % "hamcrest-all" % "1.3",
  "org.scala-lang" % "scala-reflect" % "2.11.7" % "test",
)

resolvers ++= Seq(
  //  "Sonatype OSS Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots",
  //  "Sonatype OSS Releases" at "https://oss.sonatype.org/content/repositories/releases",
  "Typesafe Repository" at "https://repo.typesafe.com/typesafe/releases/"
)
