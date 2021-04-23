import sbtrelease._
import ReleaseStateTransformations._

name := "jwt-scala"

organization := "com.github.xuwei-k"

scalacOptions ++= Seq(
  "-feature",
  "-deprecation",
  "-language:implicitConversions",
)

val Scala212 = "2.12.13"

scalaVersion := Scala212

crossScalaVersions := Seq(Scala212, "2.13.5", "3.0.0-RC1")

homepage := Some(url("https://github.com/xuwei-k/jwt-scala"))

licenses += "Apache2" -> url("http://www.opensource.org/licenses/Apache-2.0")

scmInfo := Some(ScmInfo(url("https://github.com/xuwei-k/jwt-scala"), "scm:git@github.com:xuwei-k/jwt-scala.git"))

publishMavenStyle := true

Test / publishArtifact := false

pomIncludeRepository := { _ => false }

pomExtra := (
  <developers>
    <developer>
      <id>xuwei-k</id>
      <name>Kenji Yoshida</name>
    </developer>
  </developers>)

publishTo := sonatypePublishToBundle.value

libraryDependencies ++= Seq(
  "org.scalatest" %% "scalatest" % "3.2.6" % "test",
  "com.typesafe.play" %% "play-json" % "2.9.2" cross CrossVersion.for3Use2_13,
  "commons-codec" % "commons-codec" % "1.15",
  "org.bouncycastle" % "bcprov-jdk16" % "1.46"
)

releasePublishArtifactsAction := PgpKeys.publishSigned.value

(Compile / doc / scalacOptions) ++= {
  val tag = tagOrHash.value
  CrossVersion.partialVersion(scalaVersion.value) match {
    case Some((v, _)) if v >= 3 =>
      Nil
    case _ =>
      Seq(
        "-sourcepath",
        (LocalRootProject / baseDirectory).value.getAbsolutePath,
        "-doc-source-url",
        s"https://github.com/xuwei-k/jwt-scala/tree/${tag}€{FILE_PATH}.scala"
      )
  }
}

val tagName = Def.setting {
  s"v${if (releaseUseGlobalVersion.value) (ThisBuild / version).value else version.value}"
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

def releaseStepCross[A](key: TaskKey[A]) =
  ReleaseStep(
    action = { state =>
      val extracted = Project extract state
      extracted.runAggregated(extracted.get(thisProjectRef) / (Global / key), state)
    },
    enableCrossBuild = true
  )

releaseProcess := Seq[ReleaseStep](
  checkSnapshotDependencies,
  inquireVersions,
  runClean,
  runTest,
  setReleaseVersion,
  commitReleaseVersion,
  tagRelease,
  releaseStepCross(PgpKeys.publishSigned),
  releaseStepCommandAndRemaining("sonatypeBundleRelease"),
  setNextVersion,
  commitNextVersion,
  pushChanges
)
