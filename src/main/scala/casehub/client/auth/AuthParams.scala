package casehub.client.auth

import org.http4s.circe.jsonEncoderOf
import org.http4s.EntityEncoder
import io.circe.generic.extras.Configuration

final case class AuthParams(
  audience: String,
  grantType: "client_credentials",
  clientId: String,
  clientSecret: String
)

object AuthParams {
  import io.circe._
  implicit val cfg: Configuration = Configuration.default.withSnakeCaseMemberNames
  @annotation.nowarn
  implicit val enc: Encoder[AuthParams] = io.circe.generic.extras.semiauto.deriveConfiguredEncoder[AuthParams]
  implicit def eenc[F[_]]: EntityEncoder[F, AuthParams] = jsonEncoderOf[F, AuthParams]
}
