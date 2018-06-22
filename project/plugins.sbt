// The Play plugin
addSbtPlugin("com.typesafe.play" % "sbt-plugin" % "2.4.3")

// JaCoCo (Java Code Coverage). See qa.jacoco.conf file for more details
addSbtPlugin("de.johoop" % "jacoco4sbt" % "2.1.6")

// Eclipse project file generation
addSbtPlugin("com.typesafe.sbteclipse" % "sbteclipse-plugin" % "4.0.0")

// SBT Checkstyle plugin for Static Analysis of code standards
addSbtPlugin("com.etsy" % "sbt-checkstyle-plugin" % "3.0.0")

lazy val root = Project("plugins", file("."))
                  .dependsOn(findbugsPlugin)

// SBT Findbugs plugin for Static Analysis of common coding errors
lazy val findbugsPlugin   = uri("git://github.com/Redmart/sbt-findbugs-plugin.git")

