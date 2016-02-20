name := "fca"

scalaVersion := "2.11.7"

version := "0.1"

scalacOptions ++= Seq(
  "-deprecation",
  "-encoding", "UTF-8",       // yes, this is 2 args
  "-feature",
  "-language:existentials",
  "-language:higherKinds",
  "-language:implicitConversions",
  "-unchecked",
  "-Xfatal-warnings",
  "-Xlint",
  "-Yno-adapted-args",
  "-Ywarn-dead-code",        // N.B. doesn't work well with the ??? hole
  "-Ywarn-numeric-widen",
  "-Ywarn-value-discard",
  "-Xfuture"
  // very annoying in the REPL
  //"-Ywarn-unused-import"     // 2.11 only
)

libraryDependencies ++= Seq(
  "org.scalaz" %% "scalaz-core" % "7.2.0"
)
