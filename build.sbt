organization := "org.graphstream"

name := "gs-ui"

version := "1.3"

organizationName := "The GraphStream Team"

organizationHomepage := Some(url("http://graphstream-project.org"))

homepage := Some(url("http://graphstream-project.org"))

startYear := Some(2010)

description := "The GraphStream library. With GraphStream you deal with graphs. Static and Dynamic. You create them from scratch, from a file or any source. You display and render them."

licenses += "LGPL3" -> url("http://www.gnu.org/copyleft/lesser.html")

licenses += "Cecill-C" -> url("http://www.cecill.info/licences/Licence_CeCILL-C_V1-en.html")

scalaVersion := "2.12.1"

libraryDependencies += "org.graphstream" % "gs-core" % "1.3"

libraryDependencies += "org.graphstream" % "gs-algo" % "1.3"

libraryDependencies += "junit" % "junit" % "4.12" % "test"

libraryDependencies += "com.novocode" % "junit-interface" % "0.11" % "test"