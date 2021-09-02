load("@scala_things//:dependencies/dependencies.bzl", "java_dependency", "scala_dependency", "scala_fullver_dependency", "make_scala_versions", "apply_scala_fullver_version")

scala_versions = make_scala_versions(
    "2",
    "13",
    "6",
)

project_deps = [
    scala_dependency("org.typelevel", "cats-effect", "3.2.2"),
    scala_dependency("org.typelevel", "cats-core", "2.6.1"),

    scala_dependency("co.fs2", "fs2-core", "3.1.0"),

    scala_dependency("io.github.jmcardon", "tsec-common", "0.4.0"),
    scala_dependency("io.github.jmcardon", "tsec-signatures", "0.4.0"),
    scala_dependency("io.github.jmcardon", "tsec-jwt-sig", "0.4.0"),

    scala_dependency("org.http4s", "http4s-jdk-http-client", "0.5.0-M4"),
    scala_dependency("org.http4s", "http4s-client", "1.0.0-M24"),
    scala_dependency("org.http4s", "http4s-core", "1.0.0-M24"),
    scala_dependency("org.http4s", "http4s-circe", "1.0.0-M24"),
    scala_dependency("org.http4s", "http4s-dsl", "1.0.0-M24"),

    scala_dependency("io.circe", "circe-core", "0.14.1"),
    scala_dependency("io.circe", "circe-parser", "0.14.1"),
    scala_dependency("io.circe", "circe-generic", "0.14.1"),
    scala_dependency("io.circe", "circe-generic-extras", "0.14.1"),

    scala_dependency("org.scalameta", "munit", "0.7.26"),
    scala_dependency("org.typelevel", "munit-cats-effect-3", "1.0.0"),

    scala_dependency("io.chrisdavenport", "cats-time", "0.3.4"),
]

def add_scala_fullver(s):
    return apply_scala_fullver_version(scala_versions, s)
