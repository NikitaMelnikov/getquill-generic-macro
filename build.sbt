name := "GetQuill"

version := "1.0"

scalaVersion := "2.11.8"

val buildSettings = Defaults.coreDefaultSettings ++ Seq (
  organization  := "com.github.nikitamelnikov",
  version       := "0.0.1",
  scalaVersion  := "2.11.8",
  scalacOptions += ""
)

lazy val root: Project = Project(
  "root",
  file("."),
  settings = buildSettings ++ Seq(
    libraryDependencies ++= Seq(
      "org.postgresql" % "postgresql" % "9.4.1208",
      "io.getquill" %% "quill-async" % "0.8.0"
    )
  )
) dependsOn(macros)

lazy val macros: Project = Project(
  "macros",
  file("macros"),
  settings = buildSettings ++ Seq(
    libraryDependencies <+= (scalaVersion)("org.scala-lang" % "scala-compiler" % _),
    libraryDependencies ++= Seq(
      "org.postgresql" % "postgresql" % "9.4.1208",
      "io.getquill" %% "quill-async" % "0.8.0"
    )
  )
)