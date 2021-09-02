package casehub.client.auth

import org.http4s.circe.jsonEncoderOf
import org.http4s.EntityEncoder

final case class AuthParams(
  audience: String,
  grantType: "client_credentials",
  clientId: String,
  clientSecret: String
)

object AuthParams {
  import io.circe._
  implicit val enc: Encoder[AuthParams] = io.circe.generic.semiauto.deriveEncoder[AuthParams]
  implicit def eenc[F[_]]: EntityEncoder[F, AuthParams] = jsonEncoderOf[F, AuthParams]
}
