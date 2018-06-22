import de.johoop.jacoco4sbt.JacocoPlugin._
import de.johoop.jacoco4sbt._

jacoco.settings
itJacoco.settings

val thresholds: Thresholds = Thresholds(instruction = 75, method = 95, branch = 45, complexity = 55, line = 95, clazz = 90)
val excludes: Seq[String] = Seq("*views*", "*Routes*", "*models*", "*dto*", "*constants*",
  "*controllers*routes*", "*controllers*Reverse*", "*controllers*javascript*", "*controller*ref*")

parallelExecution in jacoco.Config := false
jacoco.outputDirectory in jacoco.Config := file("target/unit-jacoco")
jacoco.reportFormats   in jacoco.Config := Seq(HTMLReport("utf-8"))
jacoco.excludes in jacoco.Config := excludes
jacoco.thresholds in jacoco.Config := thresholds

parallelExecution in itJacoco.Config := false
jacoco.outputDirectory in itJacoco.Config := file("target/jacoco")
jacoco.reportFormats in itJacoco.Config := Seq(HTMLReport("utf-8"))
jacoco.excludes in itJacoco.Config := excludes
jacoco.thresholds in itJacoco.Config := thresholds
