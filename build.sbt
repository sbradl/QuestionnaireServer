name := "QuestionnaireServer"
 
scalaVersion := "2.9.1"

resolvers += "Java.net Maven2 Repository" at "http://download.java.net/maven/2/"

resolvers += "Scala Tools Snapshots" at "http://scala-tools.org/repo-snapshots"

resolvers += "Typesafe Snapshots" at "http://repo.typesafe.com/typesafe/snapshots"
 
seq(webSettings: _*)
 
libraryDependencies ++= {
  val liftVersion = "2.4"
  Seq(
    "net.liftweb" %% "lift-webkit" % liftVersion % "compile->default",
    "net.liftweb" %% "lift-mapper" % liftVersion % "compile->default",
    "net.liftweb" %% "lift-wizard" % liftVersion % "compile->default",
    "net.liftweb" %% "lift-widgets" % liftVersion % "compile->default"
  )
}
 
libraryDependencies ++= Seq(
  "org.eclipse.jetty" % "jetty-webapp" % "8.1.0.RC2" % "container",
  "javax.servlet" % "servlet-api" % "2.5" % "provided->default",
  "com.h2database" % "h2" % "1.3.163",
  "org.slf4j" % "slf4j-log4j12" % "1.6.4"
)

scalacOptions in Compile ++= Seq(
	"-deprecation"
)
