name := "QuestionnaireServer"
 
scalaVersion := "2.9.0-1"
 
seq(webSettings: _*)

// If using JRebel
jettyScanDirs := Nil
 
libraryDependencies ++= {
  val liftVersion = "2.4-M4"
  Seq(
    "net.liftweb" %% "lift-webkit" % liftVersion % "compile->default",
    "net.liftweb" %% "lift-mapper" % liftVersion % "compile->default",
    "net.liftweb" %% "lift-wizard" % liftVersion % "compile->default",
    "net.liftweb" %% "lift-widgets" % liftVersion % "compile->default"
  )
}
 
libraryDependencies ++= Seq(
  "junit" % "junit" % "4.8.2" % "test->default",
  "org.mortbay.jetty" % "jetty" % "6.1.26" % "jetty",
  "javax.servlet" % "servlet-api" % "2.5" % "provided->default",
  "com.h2database" % "h2" % "1.3.158",
  "org.slf4j" % "slf4j-log4j12" % "1.6.1",
  "org.scalatest" % "scalatest_2.9.0-1" % "1.6.1" % "test->default",
  "org.mockito" % "mockito-all" % "1.8.5" % "test->default"
)

scalacOptions in Compile ++= Seq(
	"-deprecation"
)
