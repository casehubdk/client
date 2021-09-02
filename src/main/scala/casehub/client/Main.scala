package casehub.client

import cats.effect.IOApp
import cats.effect.{ExitCode, IO}

object Main extends IOApp {
  override def run(args: List[String]): IO[ExitCode] = IO.pure(ExitCode.Success)
}
