kantanProject in ThisBuild := "xpath"
startYear in ThisBuild     := Some(2016)

// - root projects -----------------------------------------------------------------------------------------------------
// ---------------------------------------------------------------------------------------------------------------------
lazy val root = Project(id = "kantan-xpath", base = file("."))
  .settings(moduleName := "root")
  .enablePlugins(UnpublishedPlugin)
  .settings(
    initialCommands in console :=
      """
      |import kantan.xpath._
      |import kantan.xpath.implicits._
      |import kantan.xpath.joda.time._
      |import kantan.xpath.refined._
    """.stripMargin
  )
  .aggregate(core, nekohtml, docs, laws, cats, scalaz, jodaTime, refined)
  .aggregateIf(java8Supported)(java8)
  .dependsOn(core, nekohtml, jodaTime, refined)

lazy val docs = project
  .settings(
    unidocProjectFilter in (ScalaUnidoc, unidoc) :=
      inAnyProject -- inProjectsIf(!java8Supported)(java8)
  )
  .enablePlugins(DocumentationPlugin)
  .dependsOn(core, nekohtml, cats, scalaz, jodaTime, refined)
  .dependsOnIf(java8Supported)(java8)

// - core projects -----------------------------------------------------------------------------------------------------
// ---------------------------------------------------------------------------------------------------------------------
lazy val core = project
  .settings(
    moduleName := "kantan.xpath",
    name       := "core"
  )
  // TODO: disable when we upgrade to 2.12.3, which appears to fix this issue.
  // This is necessary because with scala 2.12.x, we use too many nested lambdas for deserialisation to succeed with the
  // "optimised" behaviour.
  .settings(scalacOptions ++= (CrossVersion.partialVersion(scalaVersion.value) match {
    case Some((_, x)) if x == 12 ⇒ Seq("-Ydelambdafy:inline")
    case _                       ⇒ Seq.empty
  }))
  .enablePlugins(PublishedPlugin, spray.boilerplate.BoilerplatePlugin)
  .settings(
    libraryDependencies ++= Seq(
      "com.propensive" %% "contextual"              % Versions.contextual,
      "com.nrinaudo"   %% "kantan.codecs"           % Versions.kantanCodecs,
      "com.nrinaudo"   %% "kantan.codecs-scalatest" % Versions.kantanCodecs % "test"
    )
  )
  .laws("laws")

lazy val laws = project
  .settings(
    moduleName := "kantan.xpath-laws",
    name       := "laws"
  )
  .enablePlugins(PublishedPlugin, spray.boilerplate.BoilerplatePlugin)
  .dependsOn(core)
  .settings(libraryDependencies += "com.nrinaudo" %% "kantan.codecs-laws" % Versions.kantanCodecs)

// - joda-time projects ------------------------------------------------------------------------------------------------
// ---------------------------------------------------------------------------------------------------------------------
lazy val jodaTime = Project(id = "joda-time", base = file("joda-time"))
  .settings(
    moduleName := "kantan.xpath-joda-time",
    name       := "joda-time"
  )
  .enablePlugins(PublishedPlugin)
  .dependsOn(core, laws % "test")
  .settings(
    libraryDependencies ++= Seq(
      "com.nrinaudo" %% "kantan.codecs-joda-time"      % Versions.kantanCodecs,
      "com.nrinaudo" %% "kantan.codecs-joda-time-laws" % Versions.kantanCodecs % "test",
      "com.nrinaudo" %% "kantan.codecs-scalatest"      % Versions.kantanCodecs % "test"
    )
  )

// - java8 projects ----------------------------------------------------------------------------------------------------
// ---------------------------------------------------------------------------------------------------------------------
lazy val java8 = project
  .settings(
    moduleName := "kantan.xpath-java8",
    name       := "java8"
  )
  .enablePlugins(PublishedPlugin)
  .dependsOn(core, laws % "test")
  .settings(
    libraryDependencies ++= Seq(
      "com.nrinaudo" %% "kantan.codecs-java8"      % Versions.kantanCodecs,
      "com.nrinaudo" %% "kantan.codecs-java8-laws" % Versions.kantanCodecs % "test",
      "com.nrinaudo" %% "kantan.codecs-scalatest"  % Versions.kantanCodecs % "test"
    )
  )

// - external parsers projects -----------------------------------------------------------------------------------------
// ---------------------------------------------------------------------------------------------------------------------
lazy val nekohtml = project
  .settings(
    moduleName := "kantan.xpath-nekohtml",
    name       := "nekohtml"
  )
  .enablePlugins(PublishedPlugin)
  .dependsOn(core)
  .settings(
    libraryDependencies ++= Seq(
      "net.sourceforge.nekohtml" % "nekohtml"                 % Versions.nekoHtml,
      "com.nrinaudo"             %% "kantan.codecs-scalatest" % Versions.kantanCodecs % "test"
    )
  )

// - cats projects -----------------------------------------------------------------------------------------------------
// ---------------------------------------------------------------------------------------------------------------------
lazy val cats = project
  .settings(
    moduleName := "kantan.xpath-cats",
    name       := "cats"
  )
  .enablePlugins(PublishedPlugin)
  .dependsOn(core, laws % "test")
  .settings(
    libraryDependencies ++= Seq(
      "com.nrinaudo" %% "kantan.codecs-cats"      % Versions.kantanCodecs,
      "com.nrinaudo" %% "kantan.codecs-cats-laws" % Versions.kantanCodecs % "test",
      "com.nrinaudo" %% "kantan.codecs-scalatest" % Versions.kantanCodecs % "test"
    )
  )

// - scalaz projects ---------------------------------------------------------------------------------------------------
// ---------------------------------------------------------------------------------------------------------------------
lazy val scalaz = project
  .settings(
    moduleName := "kantan.xpath-scalaz",
    name       := "scalaz"
  )
  .enablePlugins(PublishedPlugin)
  .dependsOn(core, laws % "test")
  .settings(
    libraryDependencies ++= Seq(
      "com.nrinaudo" %% "kantan.codecs-scalaz"      % Versions.kantanCodecs,
      "com.nrinaudo" %% "kantan.codecs-scalaz-laws" % Versions.kantanCodecs % "test",
      "com.nrinaudo" %% "kantan.codecs-scalatest"   % Versions.kantanCodecs % "test"
    )
  )

// - refined project ---------------------------------------------------------------------------------------------------
// ---------------------------------------------------------------------------------------------------------------------
lazy val refined = project
  .settings(
    moduleName := "kantan.xpath-refined",
    name       := "refined"
  )
  .enablePlugins(PublishedPlugin)
  .dependsOn(core, laws % "test")
  .settings(
    libraryDependencies ++= Seq(
      "com.nrinaudo" %% "kantan.codecs-refined"      % Versions.kantanCodecs,
      "com.nrinaudo" %% "kantan.codecs-refined-laws" % Versions.kantanCodecs % "test",
      "com.nrinaudo" %% "kantan.codecs-scalatest"    % Versions.kantanCodecs % "test"
    )
  )
