import java.util.Properties
val appProperties = settingKey[Properties]("The application properties")
appProperties := {
  val prop = new Properties()
  IO.load(prop, new File("conf/application.common.conf"))
  prop
}

name := appProperties.value.getProperty("project.name")
version := appProperties.value.getProperty("project.version").replaceAll("\"", "")

lazy val root = (project in file("."))
                  .enablePlugins(PlayJava)
                  .configs(IntegrationTest)

scalaVersion := "2.11.6"

routesGenerator := InjectedRoutesGenerator

libraryDependencies ++= Seq(
  javaJdbc,
  "com.google.inject"       % "guice"                     % "4.0",
  "com.redmart.commons"     % "commons-microservice"      % "3.2.0",
  "org.codehaus.janino"     % "janino"                    % "2.7.8",  // Required by Logback to do conditional configs
  "com.redmart.commons"     % "commons-test"              % "2.0.0"     % "it,test",
  "org.mockito"             % "mockito-core"              % "1.10.19"   % "it,test",
  "junit"                   % "junit"                     % "4.12"      % "it",
  "com.typesafe.play"       % "play-test_2.11"            % play.core.PlayVersion.current % "it",
  "com.microsoft.sqlserver" % "mssql-jdbc"                % "6.2.2.jre8",
  "org.projectlombok"       % "lombok"                    % "1.16.12"
)

resolvers += Resolver.mavenLocal
resolvers += "redmart-nexus" at "http://nexus.infra.redmart.com:8081/nexus/content/groups/public/"
resolvers += "central-maven" at "http://central.maven.org/maven2/"

unmanagedSourceDirectories  in Test <+= baseDirectory(_ / "tests" / "shared")
sourceDirectories           in Test <+= baseDirectory(_ / "tests" / "unit")
javaSource                  in Test <<= baseDirectory(_ / "tests" / "unit")
resourceDirectory           in Test <<= baseDirectory(_ / "tests" / "resources")
resourceDirectories         in Test <+= baseDirectory(_ / "tests" / "resources")

Defaults.itSettings
unmanagedSourceDirectories in IntegrationTest <+= baseDirectory(_ / "tests" / "shared")
sourceDirectories          in IntegrationTest <+= baseDirectory(_ / "tests" / "integration")
javaSource                 in IntegrationTest <<= baseDirectory(_ / "tests" / "integration")
resourceDirectory          in IntegrationTest <<= baseDirectory(_ / "tests" / "resources")
resourceDirectories        in IntegrationTest <+= baseDirectory(_ / "tests" / "resources")
