package casehub.client.auth

import org.http4s.client._
import cats.effect._
import cats.implicits._
import java.time.Instant
import java.time.temporal.ChronoUnit
import org.http4s._
import cats.data.Kleisli

trait TokenManager[F[_]] {
  val getToken: F[AuthResponse]
}

object TokenManager {
  final case class ManagerState(
    time: Instant,
    response: AuthResponse
  )

  def apply[F[_]: Async](tokenUri: Uri, authParams: AuthParams): Kleisli[F, Client[F], TokenManager[F]] =
    Kleisli { client =>
      Ref.of[F, Option[ManagerState]](None).map { state =>
        new TokenManager[F] {
          val nowF = Sync[F].blocking(Instant.now())

          val refreshToken: F[AuthResponse] = {
            val respF = client.expect[AuthResponse](
              Request[F](
                method = Method.GET,
                uri = tokenUri
              ).withEntity(authParams)
            )

            for {
              resp <- respF
              now <- nowF
              _ <- state.set(Some(ManagerState(now, resp)))
            } yield resp
          }

          val getToken: F[AuthResponse] =
            state.get.flatMap {
              case None => refreshToken
              case Some(ManagerState(time, response)) =>
                nowF.flatMap { now =>
                  val isExpiredAt: Instant = time.plus((response.expiresIn * 1d).toSeconds, ChronoUnit.SECONDS)
                  if (now.isBefore(isExpiredAt)) {
                    Sync[F].pure(response)
                  } else {
                    refreshToken
                  }
                }
            }
        }
      }
    }
}
