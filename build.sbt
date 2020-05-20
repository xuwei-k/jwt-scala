name := "jwt-scala"

organization := "com.github.xuwei-k"

scalacOptions ++= Seq(
  "-feature",
  "-deprecation",
  "-language:implicitConversions",
)

val Scala211 = "2.11.12"

scalaVersion := Scala211

crossScalaVersions := Seq(Scala211, "2.12.11", "2.13.2")

homepage := Some(url("https://github.com/xuwei-k/jwt-scala"))

licenses += "Apache2" -> url("http://www.opensource.org/licenses/Apache-2.0")

scmInfo := Some(ScmInfo(url("https://github.com/xuwei-k/jwt-scala"), "scm:git@github.com:xuwei-k/jwt-scala.git"))

publishMavenStyle := true

publishArtifact in Test := false

pomIncludeRepository := { _ => false }

pomExtra := (
  <developers>
    <developer>
      <id>xuwei-k</id>
      <name>Kenji Yoshida</name>
    </developer>
  </developers>)

publishTo := {
  val nexus = "https://oss.sonatype.org/"
  if (isSnapshot.value)
    Some("snapshots" at nexus + "content/repositories/snapshots")
  else
    Some("releases" at nexus + "service/local/staging/deploy/maven2")
}

libraryDependencies ++= Seq(
  "org.scalatest" %% "scalatest" % "3.1.1" % "test",
  "com.typesafe.play" %% "play-json" % "2.7.4",
  "commons-codec" % "commons-codec" % "1.14",
  "org.bouncycastle" % "bcprov-jdk16" % "1.46"
)

releasePublishArtifactsAction := PgpKeys.publishSigned.value

scalacOptions in (Compile, doc) ++= {
  val tag = tagOrHash.value
  Seq(
    "-sourcepath",
    (baseDirectory in LocalRootProject).value.getAbsolutePath,
    "-doc-source-url",
    s"https://github.com/xuwei-k/jwt-scala/tree/${tag}€{FILE_PATH}.scala"
  )
}

val tagName = Def.setting {
  s"v${if (releaseUseGlobalVersion.value) (version in ThisBuild).value else version.value}"
}

val tagOrHash = Def.setting {
  if (isSnapshot.value) {
    sys.process.Process("git rev-parse HEAD").lineStream_!.head
  } else {
    tagName.value
  }
}

releaseTagName := tagName.value

releaseCrossBuild := true
