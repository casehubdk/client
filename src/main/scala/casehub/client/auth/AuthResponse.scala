package casehub.client.auth

import scala.concurrent.duration.FiniteDuration
import java.util.concurrent.TimeUnit
import org.http4s.circe.jsonOf
import org.http4s.EntityDecoder
import cats.effect._
import io.circe.generic.extras.Configuration

final case class AuthResponse(
  accessToken: String,
  tokenType: "Bearer",
  expiresIn: FiniteDuration
)

object AuthResponse {
  import io.circe._
  implicit val cfg: Configuration = Configuration.default.withSnakeCaseMemberNames
  @annotation.nowarn
  implicit val dec: Decoder[AuthResponse] = {
    implicit val fd: Decoder[FiniteDuration] = Decoder.decodeLong.map(l => FiniteDuration(l, TimeUnit.SECONDS))
    io.circe.generic.extras.semiauto.deriveConfiguredDecoder[AuthResponse]
  }
  implicit final def edec[F[_]: Concurrent]: EntityDecoder[F, AuthResponse] = jsonOf[F, AuthResponse]
}
